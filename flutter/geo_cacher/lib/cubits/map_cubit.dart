import 'package:equatable/equatable.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import '../services/api_repository.dart';
import '../models/cache.dart';

part 'map_state.dart';

class MapCubit extends Cubit<MapState> {
  final ApiRepository _api;

  MapCubit(this._api) : super(MapInitial());

  Future<void> fetchCaches(
    double north,
    double east,
    double south,
    double west,
  ) async {
    emit(MapLoading());
    try {
      final caches = await _api.fetchCaches(north, east, south, west);
      emit(MapSuccess(caches));
    } catch (e) {
      emit(MapFailure(e.toString()));
    }
  }

  Future<void> createCache(CacheRequest request) async {
    try {
      final created = await _api.createCache(request);
      final currentState = state;
      if (currentState is MapSuccess) {
        final updated = List<Cache>.from(currentState.caches)..add(created);
        emit(MapSuccess(updated));
      }
    } catch (e) {
      emit(MapFailure(e.toString()));
    }
  }
}
