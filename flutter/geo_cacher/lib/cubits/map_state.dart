part of 'map_cubit.dart';

sealed class MapState extends Equatable {
  const MapState();

  @override
  List<Object> get props => [];
}

final class MapInitial extends MapState {}

final class MapLoading extends MapState {}

final class MapSuccess extends MapState {
  final List<Cache> caches;

  const MapSuccess(this.caches);

  @override
  List<Object> get props => [caches];
}

final class MapFailure extends MapState {
  final String message;

  const MapFailure(this.message);

  @override
  List<Object> get props => [message];
}
