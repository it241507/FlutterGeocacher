import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

String formatDateToLocale(BuildContext context, DateTime date) {
  final locale = Localizations.localeOf(context).toString();
  return DateFormat.yMMMMEEEEd(locale).format(date);
}
