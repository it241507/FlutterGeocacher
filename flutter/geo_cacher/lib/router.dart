import 'dart:async';

import 'package:flutter/material.dart';
import 'package:geo_cacher/cubits/auth_cubit.dart';
import 'package:geo_cacher/pages/create_cache.dart';
import 'package:geo_cacher/pages/login.dart';
import 'package:geo_cacher/pages/map.dart';
import 'package:geo_cacher/pages/register.dart';
import 'package:go_router/go_router.dart';
import 'package:latlong2/latlong.dart';

class AppRoutes {
  static const map = '/';
  static const login = '/login';
  static const register = '/register';
  static const createCache = '/cache/new/:lat/:lng';
}

GoRouter createRouter(AuthCubit authCubit) {
  return GoRouter(
    initialLocation: AppRoutes.map,
    refreshListenable: GoRouterRefreshStream(authCubit.stream),
    redirect: (context, state) {
      final isLoggedIn = authCubit.state is AuthSuccess;
      final isLoggingIn = state.uri.path == AppRoutes.login;
      final isRegistering = state.uri.path == AppRoutes.register;

      if (!isLoggedIn && !isLoggingIn && !isRegistering) {
        return AppRoutes.login;
      }
      if (isLoggedIn && isLoggingIn) {
        return AppRoutes.map;
      }
      return null;
    },
    routes: [
      GoRoute(path: AppRoutes.map, builder: (context, state) => const MapPage()),
      GoRoute(path: AppRoutes.login, builder: (context, state) => const LoginPage()),
      GoRoute(
        path: AppRoutes.register,
        builder: (context, state) => const RegisterPage(),
      ),
      GoRoute(
        path: AppRoutes.createCache,
        builder: (context, state) {
          final lat = double.parse(state.pathParameters['lat']!);
          final lng = double.parse(state.pathParameters['lng']!);
          return CreateCachePage(latlng: LatLng(lat, lng));
        },
      ),
    ],
  );
}

class GoRouterRefreshStream extends ChangeNotifier {
  GoRouterRefreshStream(Stream<dynamic> stream) {
    notifyListeners();
    _subscription = stream.listen((dynamic _) => notifyListeners());
  }

  late final StreamSubscription<dynamic> _subscription;

  @override
  void dispose() {
    _subscription.cancel();
    super.dispose();
  }
}
