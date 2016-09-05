# Group Project - *mpromptu*

How long have you been stuck in routine work and losing passion in your life? It is time to make a change! New learning experiences, discovering new cities more deeply, and enjoying in the moment are great ways to save us from boredom. **mpromptu** helps make that possible! **mpromptu** is an Android app that makes experience matches based on your location in real-time. It can be your local friend to introduce new stuff to do around your area, or your smart guide when you are traveling in a new city and looking for something unique, or even a great tool to make new friends! **mpromptu** can make you realize *there are so many possibilities in life!*

Time spent: **120** hours spent in total

## User Stories

The following **required** functionality is completed:
* [x] User can sign in with Facebook/Gmail account (to browse and save experiences)
* [x] User can see navigation drawer with [Location, Category, Bookmarks, Settings, and Profile] fragments.
  * [x] User can select current location or type in location in the Location fragment (similar to Groupon).
  * [ ] User can select Category filters and show filtered experiences. User can select experience based on three categories of experience: **thrill**, **social**, and **fun** (or all)
* [x] User can switch to map view to browse experiences around him/her
  * [x] Icons show type of experience nearby
  * [x] User can tap on an experience to go to detail view
* [x] User can browse list of activities around him/her 
  * [x] User can select and save activity to list (bookmarks)
  * [x] When user clicks on a card, he/she can see detailed description, reviews, and related pictures.
  * [ ] User can use swipe gesture to swipe away an activity/experience that he/she is not interested in.
* [x] User sees detailed view of an activity
    * [x] User can view details about venue and location of experience
    * [x] User can see existing reviews and type his/her own.
* [x] User receives push notifications based on location for related experiences.
  * [x] Use Firebase/Parse to set up push notificatons.
* [x] User can share a new experience offer (maybe by clicking a Floating Action Button):
  * [x] Present this experience in the form of a card.
  * [x] User types a title with few words description.
  * [ ] User can add a 1 minute video of the experience they offer (like an intro video).
  * [ ] **Optional**: User can add a price for this experience

The following **optional** features are implemented:
* [ ] Gamification allows user to gain points based on posts they make, or things they do
  * [ ] View others' scores and see stats
* [ ] User can select **Offline mode**.
* [ ] User can select in two time-frame options: **experience over time** or **experience in the moment**.
* [ ] Incorporate APIs to provide valuable data to user.
  * [x] Use Geofencing API to understand location of user and show proximity-based cards.
  * [ ] Use (Foursquare?) API to get trending data on checkins.
  * [ ] Optional: Uber API to allow user to reach experience destination.


Here's a walkthrough of implemented user stories:

Experience cards, map view, and card detail view: 
<br>
<img src='https://github.com/rambled/experia/blob/master/experia_demo_sprint2.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

<br>

Notifications view:
<img src='http://imgur.com/uPVzIeu.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Wireframe Screenshots

<img src='http://i.imgur.com/QShMttl.jpg' title='screenshot' width="300" alt='screenshot' />
<img src='http://i.imgur.com/3skAsn9.jpg' title='screenshot' width="300" alt='screenshot' />
<img src='http://i.imgur.com/BF2KC2V.jpg' title='screenshot' width="300" alt='screenshot' />
<img src='http://i.imgur.com/zlz0zJt.jpg' title='screenshot' width="300" alt='screenshot' />
<img src='http://i.imgur.com/fCmK1mO.jpg' title='screenshot' width="300" alt='screenshot' />



## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Foursqure](https://developer.foursquare.com/) - place checking database


