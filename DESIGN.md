# PcClocks design

## Reference

Googlebook (Android 17, `lapis`) ships `SystemUIClocks-{BigNum,Calligraphy,Growth,Inflate,Metro,
NumOverlap,Weather}` in `system_ext/priv-app`. They were looked at for what each face shows
(picker thumbnails, names); nothing of them is used here. Their bundled fonts (calligraphic,
stencil, inflated and weather display faces) and Metro's animated digits are Google's assets and
are not reproduced: the faces approximate them with system fonts and drawing code.

## Plugin contract (AOSP 17)

* SystemUI: `ClockRegistry` keeps `KNOWN_PLUGINS`, package name to clock ids. A known package is
  attached without loading; it loads when its clock is selected (or in the picker, which keeps
  all loaded). Unknown packages would load at once.
* ThemePicker: `ClockRegistryProvider` builds its own `PluginManagerImpl` with these seven
  packages as privileged plugins, so its clock sheet shows them.
* Plugins need `com.android.systemui.permission.PLUGIN` (signature) and, on `user` builds, a
  place in SystemUI's `config_pluginAllowlist`.

Hence one APK per style with exactly those packages and ids (`ClockStyles`, checked by
`ClockStylesTest`).

## Code

* `PcClockProvider` (base) implements `ClockProviderPlugin` for one `ClockStyle`; the seven
  subclasses in `ClockProviders.kt` carry the `@Requires` annotation SystemUI checks.
* `PcClockController` owns a large and a small `PcClockFaceController` and a `SharedClockState`
  (time keeper, 12/24 hour format, locale, weather).
* A face controller keeps its palette (`ClockColors`: the picked seed color, or four tones of the
  wallpaper's system palette), font size (from SystemUI's `onFontSettingChanged`) and doze level;
  every change builds a `FaceState` and hands it to a `ClockFaceView`.
* The view measures and draws through a `ClockPainter`. `ClockPainters` maps styles to painters:

| Style | Large clock | Small clock |
|---|---|---|
| BigNum | `BigNumAnalogPainter` (dial 2.2 x font size) | `BigNumAnalogPainter` (1.2 x) |
| Calligraphy | `StackedDigitsPainter`, Noto Serif bold italic | `SingleLinePainter`, same font |
| Growth | `StackedDigitsPainter`, Roboto Flex wght 800 wdth 25, stencil bridges | `SingleLinePainter` |
| Inflate | `StackedDigitsPainter`, Roboto Flex wght 1000 wdth 151, 18% overlap, four tones, outline | `SingleLinePainter` |
| Metro | `TransitPainter`: `TransitGlyphs` paths, lanes 0.95/0.6/0.22 units in tones 1/2/0 | `SingleLinePainter` |
| NumOverlap | `StackedDigitsPainter`, Roboto Flex wght 1000, 22% overlap, diagonal tones | `SingleLinePainter` |
| Weather | `WeatherPainter`: time on top, date and temperature running up, line icon | `SingleLinePainter`, Google Sans Flex |

* Doze moves every tone towards the always-on display color (`TonePalette.towards`).
* The weather face reports `hasCustomWeatherDataDisplay` so the lock screen does not repeat the
  weather; the icon family comes from `WeatherData.WeatherStateIcon` ids (`WeatherIconKind`).

## Not verified without a build and a device

Compilation through Soong and R8 (the Kotlin compiles against the tree's `SystemUIPluginLib`,
`SystemUIClocks-CommonLib`, framework and ICU jars), loading by SystemUI and ThemePicker, and the
look of each face on the lock screen and in the picker (sizes and positions are first values to
tune on a device).
