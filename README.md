# RssFeed
Fetch articles from Lepoint's RSS feed

The application is handling the following statements : 

- Get rss Feed from LePoint website (XML file)
- Parse that feed
- Display a corresponding articles list (title + thumbnail)
- Pull to refresh on the list
- You can get details (desc, date) on each news by clicking on them (it'll load a new activity)

The datas are stored in a DataBase when they're fetched.
**If you're out of connection, the news will be loaded from the DB instead of the Rss Feed.**

## Home

The main view displays the 20 firsts articles from the Internet section of Le Point

![alt tag](https://github.com/GinjiBan/LePointRssFeed/blob/master/pic/Home.png)

## Detail

You can get the description and the date of each articles by clicking on it

![alt tag](https://github.com/GinjiBan/LePointRssFeed/blob/master/pic/main_click.jpg)

![alt tag](https://github.com/GinjiBan/LePointRssFeed/blob/master/pic/Detail.png)

## Landscape

Both view (main page and detail page) are working on landscape mode (**without re-feteching the datas**)

![alt tag](https://github.com/GinjiBan/LePointRssFeed/blob/master/pic/Detail_landscape.png)

## Database

Datas are stored in a SQLite DB that'll be used if you're out of connection.
The storage takes place in an AsyncTask to maintain the app performances.

![alt tag](https://github.com/GinjiBan/LePointRssFeed/blob/master/pic/db.jpg)
