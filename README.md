# AKND
Project # 2: Build an Asteroid Radar app using NASA's NeoWs API
=======
Main of this project is on properly implementing MVVM Architecture. (ViewModel, Repository, Network, and Database)

## 1. Basic setup to prepare for the rest of the project
- [x] Create NeoListFragment, NeoDetails Fragment, and ViewModels for each.
- [x] TextView in XML Layouts for basic data validation testing in later sections.

## 2. Make Retrofit call to NeoWs API
- [x] Setup Retrofit Service to call to NeoWs API.  Take the received JSON and display it in TextView to verify call is successful.
- [x] Create a NeoDTO (Data Transfer Object)
- [x] Parse JSON and map it to NeoDTO. Modify Retrofit call to return a list of NeoDTO objects.
- [x] Print contents of list to TextView to verify parsing has been implemented successfully.

## 3. Setup our NeoDatabase.
- [x] Implement synchronized(lock) to make sure we only ever have one instance of our database.
- [x] Create NeoDAO and implement required methods for accessing and writing to the database.
  - [x] add(varargs), getAll(). (Might be all we need since we're using LiveData that gets automatically updated. May add more as needed)
- [x] Create NeoEntity data class with extension function to convert to Domain data class.
- [x] Instead of displaying Retrofit data to the TextView, map it to NeoEntity and then save it into the database
- [x] Observe LiveData returned by getAll() and ensure we can read from the database by printint contents to TextView.

## 4. Setup our NeoRepository.
- [x] Move Retrofit and database code into Repository.
- [x] Pass LiveData from database to ViewModel so we always see some data.
- [x] Include refreshData method so that we can refresh the data on app startup so the user is always getting new content.

## 5. Expand UI
- [ ] Start fleshing out UI so that we display a list of the data with a RecyclerView.
- [ ] Add a clickListener to list items so that we can navigate to the Details Screen for each individual item.
  - [ ] Decide whether we'll pass the object or just the id to call from Database. (Probably object)

## 6. WorkerManager
Will likely need to review WorkerManager since we didn't have much practice with it.  Will update checklist as more becomes apparent.
- [ ] Use WorkerManager to download the data once per day, and only if charging/connected to wifi.
- [ ] Extra Credit: Have the same WorkerManager also delete Neo objects from before the current day. (If it is 5/25, then all Neo objects in the database from 5/24 or earlier should be deleted.)

## 7. TalkBack
- [ ] Test project and ensure it works with TalkBack accesibility.
