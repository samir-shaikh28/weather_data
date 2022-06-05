Description:

This weather app using a free api to fetch data. we show current location weather information, hour wise weather information and upcoming days information.

We have used /forecast.json api from https://www.weatherapi.com/ platform.

App Architechture and Package Structure:

In this app i've followed MVVM architecure. this project consist following packages:

1. di : Dependency injection related classes
2. data: it consist network, db, datastore and api responses related packages and classes.
3. listener: This package consist Callback class being used in UI
4. repository: It consist repository classes
5. ui: This package consist all classes related to UI i.e Activities, Fragments, ViewModels, ViewHolders, Adapter etc.
6. utils: This package consist utility classes
