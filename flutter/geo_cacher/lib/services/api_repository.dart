import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../constants.dart';
import '../models/cache.dart';
import '../models/auth.dart';

class ApiRepository {
  final Dio _dio;
  final SharedPreferences _prefs;
  static const _tokenKey = 'auth_token';

  ApiRepository._(this._dio, this._prefs);

  static Future<ApiRepository> create() async {
    final prefs = await SharedPreferences.getInstance();
    final dio = Dio(BaseOptions(baseUrl: baseUrl));

    final repo = ApiRepository._(dio, prefs);

    if (kDebugMode) {
      dio.interceptors.add(LogInterceptor(
        requestHeader: true,
        requestBody: true,
        responseHeader: true,
        responseBody: true,
        error: true,
        logPrint: (obj) => print(obj),
      ));
    }

    dio.interceptors.add(InterceptorsWrapper(
      onRequest: (options, handler) {
        final path = options.path ?? '';
        if (path.endsWith('/api/users/login') || path.endsWith('/api/users/registration') || path.endsWith('/api/users/logout')) {
          return handler.next(options);
        }

        final token = repo._prefs.getString(_tokenKey);
        if (token != null) {
          options.headers['Authorization'] = 'Bearer $token';
        }
        return handler.next(options);
      },
    ));

    return repo;
  }

  Future<AuthResponse> login(String email, String password) async {
    try {
      final res = await _dio.post('/api/users/login',
          data: jsonEncode({'email': email, 'password': password}),
          options: Options(headers: {'Content-Type': 'application/json'}));
      final body = res.data;
      final token = body['auth-token'] ?? body['authToken'] ?? body['token'] ?? body['auth_token'];
      if (token == null) {
        print('Login response missing token: ${body.toString()}');
        throw Exception('No token returned from server');
      }
      await _prefs.setString(_tokenKey, token);
      return AuthResponse(token: token);
    } on DioException catch (e) {
      final status = e.response?.statusCode;
      final data = e.response?.data;
      print('Login failed: status=$status data=${data?.toString()} message=${e.message}');
      String serverMessage = '';
      if (data is Map) {
        serverMessage = (data['message'] ?? data['error'] ?? data['status'] ?? '').toString();
      } else if (data != null) {
        serverMessage = data.toString();
      }
      if (serverMessage.isNotEmpty) {
        throw Exception('Login failed: $serverMessage');
      }
      throw Exception('Login failed: ${e.message} (HTTP ${status ?? 'unknown'})');
    }
  }

  Future<AuthResponse> register(String name, String email, String password) async {
    try {
      final res = await _dio.post('/api/users/registration',
          data: jsonEncode({'name': name, 'mail': email, 'pw': password}),
          options: Options(headers: {'Content-Type': 'application/json'}));
      final body = res.data;
      final token = body['auth-token'] ?? body['authToken'] ?? body['token'] ?? body['auth_token'];
      if (token == null) {
        print('Register response missing token: ${body.toString()}');
        throw Exception('No token returned from server');
      }
      await _prefs.setString(_tokenKey, token);
      return AuthResponse(token: token);
    } on DioException catch (e) {
      final status = e.response?.statusCode;
      final data = e.response?.data;
      print('Register failed: status=$status data=${data?.toString()} message=${e.message}');
      String serverMessage = '';
      if (data is Map) {
        serverMessage = (data['message'] ?? data['error'] ?? data['status'] ?? '').toString();
      } else if (data != null) {
        serverMessage = data.toString();
      }
      if (serverMessage.isNotEmpty) {
        throw Exception('Register failed: $serverMessage');
      }
      throw Exception('Register failed: ${e.message} (HTTP ${status ?? 'unknown'})');
    }
  }

  Future<List<Cache>> fetchCaches(double north, double east, double south, double west) async {
    final res = await _dio.get('/api/caches/map/$north/$east/$south/$west');
    final data = res.data as List<dynamic>;
    return data.map((e) => Cache.fromJson(e as Map<String, dynamic>)).toList();
  }

  Future<Cache> createCache(CacheRequest request) async {
    final res = await _dio.post('/api/caches', data: request.toJson(),
        options: Options(headers: {'Content-Type': 'application/json'}));
    final map = res.data as Map<String, dynamic>;
    if ((map['lat'] == null && map['latitude'] == null) || (map['lng'] == null && map['longitude'] == null)) {
      map['lat'] = request.latitude;
      map['lng'] = request.longitude;
      map['latitude'] = request.latitude;
      map['longitude'] = request.longitude;
    }
    return Cache.fromJson(map);
  }

  Future<void> logout() async {
    _prefs.remove(_tokenKey);
    try {
      await _dio.post('/api/users/logout');
    } catch (_) {}
  }
}
