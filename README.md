# Momento
“Momento” is an android application that allows for early stage person-with-dementia (PWD) to view photos, videos and voice memos from their family members.

## Table of Contents
- [How things work](README.md#how-things-work)
- [Current Build](README.md#Current-Build)
- [System Requirements](README.md#System-Requirements)



## How things work
### Getting Started
1. The App Experience begins with an **Administrator** registering an account with **email** and **password**.
2. The Administrator can then create **Patient** and **Family** accounts (email/password).
3. The Administrator links **Family** accounts (up to 6) to the appropriate **Patient** account.
4. Then **Family** account holders can login to Momento on their end to update their profile and upload multimedia for the **Patient** to view.
5. The **Patient** account holders simply need to login once to view the multimedia their **Family** members provided.

Each family member will create their own profile and upload videos of themselves answering questions to pre-selected prompts. These videos will then be viewable on the patient side of the application. Caretakers can also access the admin side of the application to view data regarding which profile and which prompts the patient is viewing most often.

Currently Momento is only available through the repository and is not available on any mobile application. Momento can be run though ndoird studio on an emulated phone (minimum android 7.0).

## Current Build 
To log into the use **"test@test.com"** **"test123"**, then click "Login"".

### Known Issues:
* After successful login, if app is closed, have to re-login.
* Registration page is not working with Firebase Authentication when form is submitted.
* Registration page "Next" button, prompting the Activity to restart.
  * Need to save last inputted information even after failed registration, so user does not need to re-enter everything again.
* "FamilyHome" page incorrectly loading functionalities of Admin Home page. This page should be the "ProfileCreation" page.
* Family "Upload" page form submission causes infinite Activity stacking. 
* Family "Upload" page Keyboard not dismissable.
* Upload currently not yet implemented.
* Patient's media playback page needs automatically close upon finished playing and return to Prompts page.
* Firebase Realtime database not implemented yet.

After the login page you will be taken to the home page where "Family" , "Patient" or "Admind" will take you to their respective home pages. In the Family home page you can see the six possible family profiles you can create at the moment. Profile 1 has a pre-loaded profile with Lebron James. Clicking on any family profile will take you to the creation/edit page where changes can be make to each profile. This feature is to fully implmented at the moment. On the Patient home page you can see created profiles and clicking on the profiles you can see the prompts. Clicking the prompts will play a placeholder video. As for the Admin home page this feature is not implemented yet and will be completed for sprint 3.
## System Requirements
minimum andoird 7.0




