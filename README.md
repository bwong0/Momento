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

## Test Accounts
To test "Admin", use "bcw7@sfu.ca" and "test123" for login.
To test "Family", use "johnnychen@family.com" and "johnnychen" for login.
To test "Patient", use "dorislow@patient.com" and "dorislow" for login.

### Known Issues:
* Admin is logged out from Firebase after registering a Patient or Family account.
* Some visual glitches for transition between Actvities.
* Images may load after a delay.

## System Requirements
minimum andoird 7.0




