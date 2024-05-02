package com.org.egglog.presentation.utils

import android.content.Context
import android.util.DisplayMetrics

const val FIGMA_WIDTH: Int = 360

const val FIGMA_HEIGHT: Int = 800
fun deviceWidth(context: Context) = context.resources.displayMetrics.widthPixels
fun deviceHeight(context: Context) = context.resources.displayMetrics.heightPixels

fun Double.pxToDp(context: Context) = this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun Int.widthPercent(context: Context) = (deviceWidth(context) * (1.0 / FIGMA_WIDTH) * this).pxToDp(context)
fun Int.heightPercent(context: Context) = (deviceHeight(context) * (1.0 / FIGMA_HEIGHT) * this).pxToDp(context)