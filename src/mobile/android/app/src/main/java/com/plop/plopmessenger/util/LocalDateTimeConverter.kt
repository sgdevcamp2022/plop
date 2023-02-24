package com.plop.plopmessenger.util

import java.time.format.DateTimeFormatter
import java.util.*

val timeFormatter = DateTimeFormatter.ofPattern("a h시 m분").withLocale(Locale.forLanguageTag("ko"))
val dayFormatter = DateTimeFormatter.ofPattern("M월 d일")
val stringParsingFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")