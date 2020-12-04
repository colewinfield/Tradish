Group project for Tradish: the authentic, ethnic food finder.

####################################################################################################
                                    Tradish App Information
####################################################################################################

Before using the application in an emulator, please set your location to Oldsmar, Florida. This way,
you'll be able to see all of the "restaurants" in the database through the application. Because this
application is crowd-sourced, there will not be any queried results in your area. Another location
that this was "tested" at is Palo Alto, California. Each will show results in their respective area.

Upon opening the app, register for a new Tradish account.

This can be accessed by clicking the "Sign Up" button option. From there, you'll need a unique email
and password to be registered into Tradish's server. Once registered, you'll be met with the
dashboard user-interface.

Note: all other times, after logging in, you won't have to log in again until you logout directly.

The dashboard has to user-interface elements: a FAB for navigation and lists of the recent spots
that you've visited and your own contributions to Tradish (places that you've shared).

The Floating Action Button (FAB) is your way to get add a new Spot and find new Spots. Spots are
Tradish's alias for "restaurants that aren't registered."

Clicking "Add new Spot" will redirect you to the form that adds a restaurant location to Tradish's
server. The form must be completed and with appropriate information. The category section has a
list of approved categories, like Mexican, Thai, Japanese, etc. This list can be expanded upon in
future updates. A description and authenticity level is also needed to move on. Authenticity levels
are ways that users signify how true-to-home the food is.

The search function gathers all Spots in your vicinity (20 kilometers) and displays them in a
searchable list. Search can be done on the title of the restaurant, its description, and its
category. Hungry for authentic Chinese food? Search "Chinese," and it'll show you all the best spots
in town.

####################################################################################################
                                        Firebase Access
####################################################################################################

Access to Tradish's Firebase console is available to the owners and Dr. Brodhead's FSU email. Anyone
that has been sent an invitation can access the information that has been submitted to the server.

The Real-Time Database houses two objects: a GeoFire location and a Restaurant. The Restaurant obj-
ect has all of the pertinent information stored. The GeoFire location object holds the restaurants
location; this is used for querying against the current user's location to find the closest results.

####################################################################################################
                                            Testing
####################################################################################################

Testing was done on a Pixel 2 (physical device) running API 30. Testing was also done through an
emulated Nexus 4 running API 19 (Kitkat), a Pixel 2 on API 28 and 29, and a Pixel 3 on API 29.                                             