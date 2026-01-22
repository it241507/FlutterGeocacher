import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:geo_cacher/cubits/auth_cubit.dart';
import 'package:geo_cacher/cubits/map_cubit.dart';
import 'package:geo_cacher/router.dart';
import 'package:go_router/go_router.dart';
import 'package:geo_cacher/services/api_repository.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final api = await ApiRepository.create();
  runApp(MyApp(api: api));
}

class MyApp extends StatefulWidget {
  final ApiRepository api;

  const MyApp({super.key, required this.api});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  late final AuthCubit _authCubit;
  late final MapCubit _mapCubit;
  late final GoRouter _router;

  @override
  void initState() {
    super.initState();
    _authCubit = AuthCubit(widget.api);
    _mapCubit = MapCubit(widget.api);
    _router = createRouter(_authCubit);
  }

  @override
  void dispose() {
    _authCubit.close();
    _mapCubit.close();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MultiBlocProvider(
      providers: [
        BlocProvider.value(value: _authCubit),
        BlocProvider.value(value: _mapCubit),
      ],
      child: MaterialApp.router(
        title: 'GeoCacher',
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(seedColor: Colors.green),
          useMaterial3: true,
        ),
        routerConfig: _router,
      ),
    );
  }
}
