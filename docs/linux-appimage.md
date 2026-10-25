# Linux AppImage packaging

BitChord currently has Android app targets and shared Kotlin/JVM code, but a shared UI library is not a standalone desktop application. To produce a runnable AppImage, a Compose Desktop application module is required in `desktop/` with an application entry point and host integrations for the app's services, persistence, and playback.

## Workflow

The `Linux AppImage` GitHub Actions workflow is isolated to the `build/linux-appimage` branch and can be started manually from **Actions → Linux AppImage → Run workflow**. It intentionally fails early with a clear message until `desktop/build.gradle.kts` and a Kotlin `main()` entry point exist. It will then package the desktop release as an AppImage and upload the binary plus a SHA-256 checksum as workflow artifacts.

## Run an AppImage

Once an artifact has been produced:

```bash
chmod +x BitChord-x86_64.AppImage
./BitChord-x86_64.AppImage
```

The AppImage target is x86_64 Linux. The workflow packages only the Linux desktop target. Android-specific workflows were removed from this branch as requested; the main branch remains unchanged.


## Source links backup

The Sources screen includes **Export source links** and **Restore source links**. Export creates a JSON file containing user-added source configurations; restore validates the complete file before replacing user-added configurations. Built-in sources are left alone. Source URLs are included as configured, so review the file before sharing it if a URL may contain a token or credential.
