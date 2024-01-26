# Okey Scores
### An app to track team scores in Turkish "Okey" Game.
Firebase FCM, Auth, Firestore, Realtime-Db, Dagger Hilt used, MVVM structure.

**App Description**
- Basically, there is a login functions and creating new user. You create new match and add match scores, but before that you need to select players that you're playing with.
- You enter their usernames and if the usernames are valid you can create a match with this users and save it to firestore.
- You can see your all matches in your app, also you can see matches that is not created by you but when you're a player in that.
- Firebase Cloud Messaging service saves user token dynamically, this feature for using notifications.

**Need to Develop that Features**
- User profile picture upload is not enabled and coded.
- There is a bug that you can use same usernames when creating an account.
- FCM needs to be developed more.
- There is 3 main fragments in bottomnavigation but only 2 works for now.
- Need some UI updates.
- Profile Fragments Textviews are not fully working. There is no registration date for now.
- Password changing is not working.

## Some Screenshots.
![popup_screen (1)](https://github.com/mertcoder/OkeyScores/assets/142554993/724d5093-b7af-4cc7-a6a9-f309c31c598c)
![login_screen](https://github.com/mertcoder/OkeyScores/assets/142554993/a77b8277-24db-443d-ac27-ceca42d4ee38)
![create_screen](https://github.com/mertcoder/OkeyScores/assets/142554993/dae65fc9-37aa-4037-b185-6cedbba3906a)
![matches_screen](https://github.com/mertcoder/OkeyScores/assets/142554993/38bfaa2d-a29d-4814-9e5a-b4a0453397c9)
![profile_screen](https://github.com/mertcoder/OkeyScores/assets/142554993/a25b87e2-c42f-4fa9-a961-dffda4488744)
![signup](https://github.com/mertcoder/OkeyScores/assets/142554993/bcdef04b-0851-40dd-a709-b21820c81d7a)
