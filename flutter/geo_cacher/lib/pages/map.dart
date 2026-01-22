import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:geo_cacher/cubits/map_cubit.dart';
import 'package:geo_cacher/pages/cache_details_sheet.dart';
import 'package:go_router/go_router.dart';
import 'package:latlong2/latlong.dart';

class MapPage extends StatefulWidget {
  const MapPage({super.key});

  @override
  State<MapPage> createState() => _MapPageState();
}

class _MapPageState extends State<MapPage> {
  final MapController _mapController = MapController();

  void _fetchCaches() {
    final bounds = _mapController.camera.visibleBounds;
    context.read<MapCubit>().fetchCaches(
      bounds.north,
      bounds.east,
      bounds.south,
      bounds.west,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text("Map"),
      ),
      body: FlutterMap(
        mapController: _mapController,
        options: MapOptions(
          minZoom: 2.5,
          initialZoom: 13,
          initialCenter: const LatLng(48.213753110136466, 15.631694122972178),
          onMapReady: _fetchCaches,
          onMapEvent: (event) {
            if (event is MapEventMoveEnd ||
                event is MapEventRotateEnd ||
                event is MapEventFlingAnimationEnd ||
                event is MapEventDoubleTapZoomEnd) {
              _fetchCaches();
            }
          },
          onTap: (tapPosition, point) {
            context.push("/cache/new/${point.latitude}/${point.longitude}");
          },
        ),
        children: [
          TileLayer(
            urlTemplate: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
            userAgentPackageName: 'at.it241515.geocacher',
          ),
          BlocBuilder<MapCubit, MapState>(
            builder: (context, state) {
              final markers = <Marker>[];
              if (state is MapSuccess) {
                markers.addAll(
                  state.caches
                    .where((cache) => cache.latitude.isFinite && cache.longitude.isFinite)
                    .map(
                    (cache) => Marker(
                      point: LatLng(cache.latitude, cache.longitude),
                      child: IconButton(
                        icon: const Icon(Icons.location_on),
                        color: Colors.red,
                        onPressed: () {
                          showBottomSheet(
                            context: context,
                            backgroundColor: Theme.of(
                              context,
                            ).colorScheme.surfaceBright,
                            builder: (context) =>
                                CacheDetailsSheet(cache: cache),
                          );
                        },
                      ),
                    ),
                  ),
                );
              }
              return MarkerLayer(markers: markers);
            },
          ),
        ],
      ),
    );
  }
}
