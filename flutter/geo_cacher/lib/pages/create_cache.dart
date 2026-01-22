import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:geo_cacher/cubits/map_cubit.dart';
import 'package:geo_cacher/models/cache.dart';
import 'package:go_router/go_router.dart';
import 'package:image_picker/image_picker.dart';
import 'package:latlong2/latlong.dart';

class CreateCachePage extends StatefulWidget {
  final LatLng latlng;

  const CreateCachePage({super.key, required this.latlng});

  @override
  State<CreateCachePage> createState() => _CreateCachePageState();
}

class _CreateCachePageState extends State<CreateCachePage> {
  final _formKey = GlobalKey<FormState>();
  final _titleController = TextEditingController();
  final _descriptionController = TextEditingController();
  File? _image;
  bool _isSubmitting = false;

  @override
  void dispose() {
    _titleController.dispose();
    _descriptionController.dispose();
    super.dispose();
  }

  Future<void> _pickImage() async {
    final picker = ImagePicker();
    final pickedFile = await picker.pickImage(source: ImageSource.gallery);

    if (pickedFile != null) {
      setState(() {
        _image = File(pickedFile.path);
      });
    }
  }

  Future<void> _submit(MapCubit mapCubit) async {
    if (!_formKey.currentState!.validate()) return;

    setState(() => _isSubmitting = true);

    try {
      String? imageBase64;
      if (_image != null) {
        final bytes = await _image!.readAsBytes();
        imageBase64 = base64Encode(bytes);
      }

      final req = CacheRequest(
        title: _titleController.text,
        description: _descriptionController.text.isEmpty ? null : _descriptionController.text,
        latitude: widget.latlng.latitude,
        longitude: widget.latlng.longitude,
        imageBase64: imageBase64,
      );

      await mapCubit.createCache(req);

      if (mounted) {
        context.pop();
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(SnackBar(content: Text('Error creating cache: $e')));
      }
    } finally {
      if (mounted) {
        setState(() => _isSubmitting = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final mapCubit = context.read<MapCubit>();

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text("New Cache"),
      ),
      body: Form(
        key: _formKey,
        child: ListView(
          padding: const EdgeInsets.all(16.0),
          children: [
            Text(
              "Coordinates: ${widget.latlng.latitude.toStringAsFixed(6)}, ${widget.latlng.longitude.toStringAsFixed(6)}",
            ),
            const SizedBox(height: 16),
            TextFormField(
              controller: _titleController,
              decoration: const InputDecoration(labelText: 'Title'),
              validator: (value) => (value == null || value.isEmpty)
                  ? 'Please enter a title'
                  : null,
            ),
            const SizedBox(height: 16),
            TextFormField(
              controller: _descriptionController,
              decoration: const InputDecoration(labelText: 'Description'),
              maxLines: 3,
            ),
            const SizedBox(height: 16),
            if (_image != null)
              Image.file(_image!, height: 200, fit: BoxFit.cover),
            TextButton.icon(
              onPressed: _pickImage,
              icon: const Icon(Icons.image),
              label: const Text("Pick Image"),
            ),
            const SizedBox(height: 24),
            ElevatedButton(
              onPressed: _isSubmitting ? null : () => _submit(mapCubit),
              child: _isSubmitting
                  ? const CircularProgressIndicator()
                  : const Text("Create"),
            ),
          ],
        ),
      ),
    );
  }
}
