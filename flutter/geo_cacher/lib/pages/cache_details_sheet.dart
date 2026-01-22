import 'package:flutter/material.dart';
import 'package:geo_cacher/constants.dart';
import 'package:geo_cacher/extensions/padding_extension.dart';
import 'package:geo_cacher/models/cache.dart';

import '../helpers/date_time.dart';

class CacheDetailsSheet extends StatelessWidget {
  final Cache cache;

  const CacheDetailsSheet({super.key, required this.cache});

  @override
  Widget build(BuildContext context) {
    return DraggableScrollableSheet(
      initialChildSize: 0.4,
      minChildSize: 0.2,
      maxChildSize: 0.9,
      expand: false,
      builder: (context, scrollController) {
        return SingleChildScrollView(
          controller: scrollController,
          child: Column(
            spacing: 16,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Center(
                child: Container(
                  width: 40,
                  height: 5,
                  decoration: BoxDecoration(
                    color: Theme.of(
                      context,
                    ).colorScheme.onSurface.withAlpha(30),
                    borderRadius: BorderRadius.circular(10),
                  ),
                ),
              ),

              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    cache.title,
                    style: Theme.of(context).textTheme.headlineMedium,
                  ),
                  Text(
                    'by ${cache.authorName}',
                    style: Theme.of(context).textTheme.bodySmall,
                  ),
                ],
              ),

              if (cache.imageFilename != null)
                ClipRRect(
                  borderRadius: BorderRadius.circular(8),
                  child: Image.network(
                    '$baseUrl/api/caches/images/${cache.imageFilename}',
                    height: 200,
                    width: double.infinity,
                    fit: BoxFit.cover,
                    errorBuilder: (context, error, stackTrace) => Container(
                      height: 200,
                      width: double.infinity,
                      color: Theme.of(
                        context,
                      ).colorScheme.onSurface.withAlpha(30),
                      child: const Icon(Icons.image_not_supported),
                    ),
                  ),
                ),

              Column(
                spacing: 4,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Description',
                    style: Theme.of(context).textTheme.titleMedium,
                  ),
                  Text(
                    cache.description ?? 'No description provided.',
                    style: Theme.of(context).textTheme.bodyMedium,
                  ),
                ],
              ),

              Row(
                spacing: 4,
                children: [
                  const Icon(Icons.location_on, size: 16),
                  Text(
                    '${cache.latitude.toStringAsFixed(6)}, ${cache.longitude.toStringAsFixed(6)}',
                    style: Theme.of(context).textTheme.bodySmall,
                  ),
                ],
              ),

              Row(
                spacing: 4,
                children: [
                  const Icon(Icons.access_time, size: 16),
                  Text(
                    formatDateToLocale(context, cache.timestamp),
                    style: Theme.of(context).textTheme.bodySmall,
                  ),
                ],
              ),
            ],
          ).padding(EdgeInsets.all(16.0)),
        );
      },
    );
  }
}
