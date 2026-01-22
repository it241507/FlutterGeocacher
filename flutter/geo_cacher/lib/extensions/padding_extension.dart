import 'package:flutter/material.dart';

extension PaddingExtension on Widget {
  Widget padding(EdgeInsetsGeometry edgeInsetsGeometry) =>
      Padding(padding: edgeInsetsGeometry, child: this);
}
