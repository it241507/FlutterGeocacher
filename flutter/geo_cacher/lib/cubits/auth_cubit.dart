import 'package:equatable/equatable.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import '../services/api_repository.dart';
import '../models/auth.dart';

part 'auth_state.dart';

class AuthCubit extends Cubit<AuthState> {
  final ApiRepository _api;

  AuthCubit(this._api) : super(AuthInitial());

  Future<void> login(String email, String password) async {
    emit(AuthLoading());
    try {
      final res = await _api.login(email, password);
      emit(AuthSuccess(res));
    } catch (e) {
      emit(AuthFailure(e.toString()));
    }
  }

  Future<void> register(String name, String email, String password) async {
    emit(AuthLoading());
    try {
      final res = await _api.register(name, email, password);
      emit(AuthSuccess(res));
    } catch (e) {
      emit(AuthFailure(e.toString()));
    }
  }
}
