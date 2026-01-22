import 'package:flutter/foundation.dart';

class Cache {
  final int id;
  final String title;
  final String? description;
  final String? authorName;
  final String? imageFilename;
  final double latitude;
  final double longitude;
  final DateTime timestamp;

  Cache({
    required this.id,
    required this.title,
    this.description,
    this.authorName,
    this.imageFilename,
    required this.latitude,
    required this.longitude,
    required this.timestamp,
  });

  factory Cache.fromJson(Map<String, dynamic> json) {
    final latRaw = json['lat'] ?? json['latitude'];
    final lngRaw = json['lng'] ?? json['longitude'];
    double lat = double.nan;
    double lng = double.nan;
    if (latRaw != null) {
      lat = (latRaw as num).toDouble();
    }
    if (lngRaw != null) {
      lng = (lngRaw as num).toDouble();
    }

    final timestampRaw = json['timeStamp'] ?? json['timestamp'];

    return Cache(
      id: json['id'] is int ? json['id'] : int.parse(json['id'].toString()),
      title: json['title'] ?? '',
      description: json['desc'] ?? json['description'],
      authorName: json['userName'] ?? json['authorName'] ?? json['author_name'],
      imageFilename: json['imageFilename'] ?? json['image_filename'],
      latitude: lat,
      longitude: lng,
      timestamp: DateTime.parse(timestampRaw),
    );
  }
}

class CacheRequest {
  final String title;
  final String? description;
  final double latitude;
  final double longitude;
  final String? imageBase64;

  CacheRequest({
    required this.title,
    this.description,
    required this.latitude,
    required this.longitude,
    this.imageBase64,
  });

  Map<String, dynamic> toJson() => {
        'title': title,
        'desc': description,
        'lat': latitude,
        'lng': longitude,
        'latitude': latitude,
        'longitude': longitude,
        'imageBase64': imageBase64,
      };
}
