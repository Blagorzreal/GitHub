#GitHub app

### Build Targets
The GitHub Android App uses `product flavors` in the gradle files to define
different build targets which allow the app to be built and point at different
environments (mocked or production) by specifying the correct `product flavor`
to build.

#### Building from Android Studio
To change what Android Studio builds, you must use the `Build Variants` setting
in Android Studio (this defaults to the mocked configuration of the build that
does not actually make network requests and does not uses DB).

### Structure of the Android Projects
There is 1 module that is built as part of the GitHub Android App.
*  `app` - Contains logic for everyting (views, models, preferences, view models, remote apis, databases).

### Known issues in latest version
* Mocked environment can be improved drastically.

### TODO
* User "following" stuff is not implemented.
* Repository "contributors" stuff is not implemented.