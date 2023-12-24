# Clash-Royale-Tournament-System
 
## Authentication
<img width="803" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/fb98c5cd-17db-4de5-96eb-1d4ccb41b2f4">

#### There was implemented a logic for TournamentStaff and Player which has different screens.

To login via Player use any PlayerID (E.g. **P1236** ) from UserCredentials.csv and default password: **password**
To login via Player use any TournamentStaffID (E.g. **T0001** ) from UserCredentials.csv and default password: **admin**


### Player Screen:
<img width="798" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/af17d307-8810-465f-9cb7-2b53e396bb3b">

The player can see his personal details and match statistics.

### Tournament Staff Screen:
<img width="803" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/777a3ee5-d818-4473-b367-2284f3fc1347">

#### Tournament Staff can Create Matches, Manage Players and Generate Reports

### Error handling:
<img width="754" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/e18b5afc-07bf-47b0-8b76-1308277b0e55">

<img width="808" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/2353a9f4-4492-4903-984d-b6529467f71d">


### Player Managing Functionality 
<img width="651" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/26e91d06-9a4d-4e41-b528-17a65485819e">
<img width="812" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/251da8b5-0064-4065-be20-eaee9c185e66">
<img width="815" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/2affe579-761a-4477-b765-ce475dc63f12">


### Report Generations
<img width="710" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/9ee6ccc5-3211-4ed9-bf94-d4b68e88583e">

#### Detailed Report 
<img width="1003" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/9132aaee-8235-4971-a66b-fca74501bedd">

#### Report Sorted By Total Points 
<img width="728" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/9871abfb-78d4-4e4d-ab2e-618e29d0e0eb">

#### Report Sorted By Total Wins
<img width="647" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/8fa7c6d1-e037-4b49-bc6a-c0e73cf63146">

#### Report Sorted By Average Score
<img width="700" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/ab00a69f-237f-4786-aa56-e49bd5fe28ba">

#### Report Sorted By Weighted Score
<img width="628" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/a6613d18-7a1a-4e03-b4f5-0874c3d4aaf9">

#### Report Sorted By Amount of Matches
<img width="627" alt="image" src="https://github.com/MykhailoDamian/Clash-Royale-Tournament-System/assets/154601293/5e2c6564-7cdc-460e-909b-cf386b5b62b5">



## How Weighting Works?
If a player has a Score of 3 which is the max in the match means he "Won", so no additional scores are added. 
If a player has a Score of 0 which is the min in the match means he "Lost", so no additional scores are added. 
If a player scores 1 or 2, we need the help of an additional database that records each match and its results. 
If a player has a Score of 1 or 2, but the result is a Win. The score adds up to 3.
If a player has a Score of 1 or 2, but the result is a Loss. The score has no effect.
