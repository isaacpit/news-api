# News API 

This API is a news API client which is a wrapper on top of existing News APIs. The external news client used was [News API](https://newsapi.org/docs). There are two main endpoints defined: 
1. An API to search news articles via keyword/author in article's descriptions, titles, and contents -  `GET /news/api/search` API
2. An API to return top headlines with support for keyword search as well as paging (if the client supports it. e.g. GNews API does not support paging with the free tier) - `GET /news/api/top-headlines` API

## `GET /news/api/search` API
| parameter name | parameter type | required       | description                                                                                                                       | allowed values                                                                                                                                                                                               | default                                     |  
|----------------|----------------|----------------|-----------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------|
| `query`        | string         | **_False_** ** | query on keyword(s)                                                                                                               | any string                                                                                                                                                                                                   | n/a                                         |                         
| `author`       | string         | **_False_** ** | query on author via article's standard keyword fields (the news API clients used don't support querying on author field directly) | any string                                                                                                                                                                                                   | n/a                                         |
| `searchIn`     | string         | False          | restrict what parts of articles the keyword/authors are searched against                                                          | `title`, `description`, `content` comma separated for multiple values e.g. `title,description,content` <br/><br/>note: same ones supported by the external news client, these values are just passed through | `null` (News API default is "title,content" |
| `client`       | string         | False          | parameter to select which News Client to use                                                                                      | `gnews`, `news`, `mock`                                                                                                                                                                                      | `news`                                      |

### Legend 
| Legend | description                                                                                                                               |
|--------|-------------------------------------------------------------------------------------------------------------------------------------------|
| **     | `query` or `author` parameters are mandatory, both can also be used and are separated by an "OR" keyword when sent to the external client |

## `GET /news/api/top-headlines` API

| parameter name | parameter type | required | description                                                                                                        | allowed values          | default                                      |  
|----------------|----------------|----------|--------------------------------------------------------------------------------------------------------------------|-------------------------|----------------------------------------------|
| `query`        | string         | False    | query on keyword(s)                                                                                                | any string              | n/a                                          |                         
| `page`         | integer        | False    | property used to page through results                                                                              | any string              | n/a                                          |
| `pageSize`     | integer        | False    | number of articles to return in a single fetch, limited by the external API as well as explicit constraint of <100 | integers <= 100         | `null` (News API default is "title,content") |
| `client`       | string         | False    | parameter to select which News Client to use                                                                       | `gnews`, `news`, `mock` | `news`                                       |

## News API & GNews API support
I also put support for [GNews API](https://gnews.io/docs/v4#introduction) feel free to just ignore the GNews client implementation, it's only used if explicitly requested via the `client=gnews` query param


### Request Translations
* This table shows the translations or equivalents between my news API, News API .org and GNews API  

| My News API field | News API.org field | GNews API field | Description                |
|-------------------|--------------------|-----------------|----------------------------|
| query             | q                  | q               | keywords query             |
| author            | q                  | q               | query on author            |
| searchIn          | searchIn           | in              | parts of article to search |
| pageSize          | pageSize           | max             | page request sizes         |
| page              | page               | page            | page number                |

# How to run server:
* The application runs using Spring Boot's NettyWebServer running on default `port 8080` 

### Requirements:
To run this project you need the following:
1. Java 21

### Server local run option #1: [exec-maven-plugin](https://www.mojohaus.org/exec-maven-plugin/usage.html)
* Populate the API keys in the following environment variables
  * News API key environment variable name = `NEWS_API_KEY`
  * GNews API key environment variable name = `GNEWS_API_KEY`

1. Option #1A - Run with environment variables set via `export`
```shell
export NEWS_API_KEY="***"
export GNEWS_API_KEY="***"
./mvnw compile exec:java -Dexec.mainClass="com.isaacpit.news.api.NewsApiApplication"  
```
2. Option #1B - Run with environment variables set in-line
```shell
NEWS_API_KEY="***" GNEWS_API_KEY="***" ./mvnw exec:java -Dexec.mainClass="com.isaacpit.news.api.NewsApiApplication"  
```
* The service will still start without the environment variables set but you will receive a `401 Unauthorized:...` or similar from the external clients 

### Server local run option #2: IntelliJ
* Navigate to `src/main/java/com/isaacpit/news/api/NewsApiApplication.java` in Intellij and click the Green Play button and run 
  * Populate the environment variables mentioned above via "Run Configurations"

### Unit/Integration Testing
* I just hit the API manually via postman and local perf test for the most part. I did add groovy/jacoco support as well but only wrote a few unit tests. In practice I would aim for >80% line coverage. Just wanted to set up the boiler plate to use this project as a boiler plate.

```shell
./mvnw clean verify package
```
  * check `target/site/jacoco.html` for details on the coverage. See the output from the `test` phase to see the Spock tests that ran 

# How to run local test client:
* There is a performance test client at `src/test/java/com/isaacpit/news/api/TestClient.groovy`

```shell
./mvnw -Dexec.classpathScope=test test-compile exec:java -Dexec.mainClass="com.isaacpit.news.api.TestClient"
```

### Sample Requests & Responses

### `GET /search` #1  - Query by keyword in title
####  *Request*
```shell
http://localhost:8080/news/api/search?query="NBA"&searchIn=title
```

#### *Response*
```json
{
  "status": "ok",
  "totalArticles": 957,
  "numArticlesReturned": 100,
  "client": "news",
  "articles": [
    {
      "title": "Wemby, Holmgren headline NBA All-Rookie team",
      "author": null,
      "description": "The Spurs' Victor Wembanyama and Thunder's Chet Holmgren were unanimous selections for the NBA's All-Rookie team.",
      "content": "May 20, 2024, 02:24 PM ET\r\nNEW YORK -- The San Antonio Spurs' Victor Wembanyama was a unanimous selection for the NBA's All-Rookie team this season, the league revealed Monday.\r\nIt was no surprise, g… [+2224 chars]",
      "url": null,
      "image": "https://a2.espncdn.com/combiner/i?img=%2Fphoto%2F2024%2F0124%2Fr1281889_1296x729_16%2D9.jpg",
      "publishedAt": "2024-05-20T18:36:19Z",
      "source": {
        "name": "ESPN",
        "url": null,
        "id": "espn"
      }
    },
    {
      "title": "In Defense of the Ad Stirring Outrage Among NBA Fans",
      "author": "Sean Gregory",
      "description": "Why are fans so upset about the AT&T ad with Oklahoma City Thunder stars Shai Gilgeous-Alexander and Chet Holmgren singing a version of the Christina Aguilera hit?",
      "content": "Perhaps the ad, which has run on an endless loop since the start of March Madness, just irks you. Maybe at this point its haunting your dreams, your soul, every freaking fiber of your DNA.\r\nIf youve … [+7253 chars]",
      "url": null,
      "image": "https://api.time.com/wp-content/uploads/2024/05/What-A-Pro-Wants.png?w=1200&h=628&crop=1",
      "publishedAt": "2024-05-02T17:54:56Z",
      "source": {
        "name": "Time",
        "url": null,
        "id": "time"
      }
    }
  ],
  "error": null
}
``` 

### `GET /search` #2 - Query by multiple keywords in title using 'AND' operator
####  *Request*
```shell
GET http://localhost:8080/news/api/search?query="NBA" AND "Finals"&searchIn=title
```

#### *Response*
```json
{
  "status": "ok",
  "totalArticles": 957,
  "numArticlesReturned": 100,
  "client": "news",
  "articles": [
    {
      "title": "NBA Finals: Minnesota Timberwolves beat Dallas Mavericks in Game 4",
      "author": "Al Jazeera",
      "description": "If Minnesota win the playoff series, they will become the first team in NBA history to advance after trailing 3-0.",
      "content": "The Minnesota Timberwolves stay alive in the NBA Western Conference finals, riding on Anthony Edwards game-high 29 points and Karl-Anthony Townss long-range shooting form for a 105-100 victory over t… [+2855 chars]",
      "url": null,
      "image": "https://www.aljazeera.com/wp-content/uploads/2024/05/AP24150125193990-1716961254.jpg?resize=1920%2C1440",
      "publishedAt": "2024-05-29T05:55:33Z",
      "source": {
        "name": "Al Jazeera English",
        "url": null,
        "id": "al-jazeera-english"
      }
    },
    {
      "title": "NBA Fans Expect Tatum, Celtics to Cruise to Finals After G1 Win vs. Mitchell, Cavs",
      "author": "Scott Polacek",
      "description": "The Boston Celtics are three wins away from their sixth Eastern Conference Finals appearance in eight years. Despite playing without Kristaps Porziņģis…",
      "content": "Barry Chin/The Boston Globe via Getty Images\r\nThe Boston Celtics are three wins away from their sixth Eastern Conference Finals appearance in eight years.\r\nDespite playing without Kristaps Porziis (c… [+4175 chars]",
      "url": null,
      "image": "https://media.bleacherreport.com/image/upload/c_fill,g_faces,w_3800,h_2000,q_95/v1715130502/pyzh23ewsk3izcljtijq.jpg",
      "publishedAt": "2024-05-08T01:20:23Z",
      "source": {
        "name": "Bleacher Report",
        "url": null,
        "id": "bleacher-report"
      }
    }
  ],
  "error": null
}
``` 

### `GET /search` #3 - Query by multiple keywords in description or content using 'AND' operator
####  *Request*
```shell
GET http://localhost:8080/news/api/search?query="NBA" AND "Finals"&searchIn=content,description
```

#### *Response*
```json
{
  "status": "ok",
  "totalArticles": 957,
  "numArticlesReturned": 100,
  "client": "news",
  "articles": [
    {
      "title": "Texas two-step? Stars motivated by Mavs' march",
      "author": "Greg Wyshynski",
      "description": "The Dallas Stars need two more wins to advance to the Stanley Cup Finals, but have gotten extra motivation in seeing arena-mates the Mavericks reach the NBA Finals.",
      "content": "DALLAS -- The Dallas Mavericksadvanced to the NBA Finals on Thursday night. The Dallas Stars said that's extra motivation to advance to their own championship round -- and potentially make sports his… [+3522 chars]",
      "url": null,
      "image": "https://a3.espncdn.com/combiner/i?img=%2Fphoto%2F2024%2F0526%2Fr1337805_1296x729_16%2D9.jpg",
      "publishedAt": "2024-05-31T17:51:02Z",
      "source": {
        "name": "ESPN",
        "url": null,
        "id": "espn"
      }
    },
    {
      "title": "Biggest takeaways from Mavericks-Timberwolves Game 1 of the West finals",
      "author": "NBA Insiders",
      "description": "Our NBA insiders break down their biggest takeaways from Game 1 of the Western Conference finals in Minneapolis.",
      "content": "May 23, 2024, 01:00 AM ET\r\nThe NBA's Western Conference finals got off to a dynamic start as the Dallas Mavericks took Game 1 against the Minnesota Timberwolves in a Minneapolis thriller on Wednesday… [+5837 chars]",
      "url": null,
      "image": "https://a4.espncdn.com/combiner/i?img=%2Fphoto%2F2024%2F0523%2Fr1336486_1296x729_16%2D9.jpg",
      "publishedAt": "2024-05-23T05:50:16Z",
      "source": {
        "name": "ESPN",
        "url": null,
        "id": "espn"
      }
    }
  ],
  "error": null
}
``` 

### `GET /search` #4 - Query by author using first and last name
####  *Request*
```shell
GET http://localhost:8080/news/api/search?author="Devindra" AND "Hardawar"&searchIn=title,content,description
```

#### *Response*
```json
{
  "status": "ok",
  "totalArticles": 957,
  "numArticlesReturned": 100,
  "client": "news",
  "articles": [
    {
      "title": "Engadget Podcast: Kill the Rabbit (R1)",
      "author": "Devindra Hardawar",
      "description": "The Rabbit R1 is finally here, and it's yet another useless AI gadget. Sure, at $199 with no monthly fee, it's a lot cheaper than the $699 Humane AI Pin. But the R1 is slow, hard to use, and doesn't actually do much. The much-promised \"Large Action Model\" mos…",
      "content": "If you click 'Accept all', we and our partners, including 237 who are part of the IAB Transparency &amp; Consent Framework, will also store and/or access information on a device (in other words, use … [+678 chars]",
      "url": null,
      "image": null,
      "publishedAt": "2024-05-03T10:30:09Z",
      "source": {
        "name": "Yahoo Entertainment",
        "url": null,
        "id": null
      }
    },
    {
      "title": "Surface Pro 10 for Business review: A safe upgrade for IT workers",
      "author": "Devindra Hardawar",
      "description": "I knew what to expect from the Surface Pro 10 for Business the minute Microsoft announced it: A faster processor with a neural processing unit (NPU) inside the same case as the Surface Pro 9. As the first \"AI PC\" Surface devices, the Pro 10 for Business table…",
      "content": "If you click 'Accept all', we and our partners, including 238 who are part of the IAB Transparency &amp; Consent Framework, will also store and/or access information on a device (in other words, use … [+678 chars]",
      "url": null,
      "image": null,
      "publishedAt": "2024-05-17T17:20:33Z",
      "source": {
        "name": "Yahoo Entertainment",
        "url": null,
        "id": null
      }
    }
  ],
  "error": null
}
``` 

### `GET /top-headlines` #1 - Query top headlines
####  *Request*
```shell
GET http://localhost:8080/news/api/top-headlines
```

#### *Response*
```json
{
  "status": "ok",
  "totalArticles": 96,
  "numArticlesReturned": 2,
  "client": "news",
  "articles": [
    {
      "title": "Boeing Starliner spacecraft launch scrubbed minutes before liftoff - The Washington Post",
      "author": "Christian Davenport",
      "description": "Boeing’s Starliner spacecraft is set to make it’s first crewed flight to the International Space Station after multiple delays.",
      "content": "Boeings first attempt to fly its Starliner spacecraft with astronauts on board was scrubbed Saturday less than four minutes before it was to lift off. The delay was called by an automated computer sy… [+3178 chars]",
      "url": null,
      "image": "https://www.washingtonpost.com/wp-apps/imrs.php?src=https://arc-anglerfish-washpost-prod-washpost.s3.amazonaws.com/public/CJRMTH4L326VOHSXQSMNJJQKRI_size-normalized.jpg&w=1440",
      "publishedAt": "2024-06-01T22:24:52Z",
      "source": {
        "name": "The Washington Post",
        "url": null,
        "id": "the-washington-post"
      }
    },
    {
      "title": "Angelina Jolie and Brad Pitt's daughter Shiloh files to drops 'Pitt' from last name - NBC News",
      "author": "Chloe Melas",
      "description": "Shiloh Jolie-Pitt, the 18-year-old daughter of Brad Pitt and Angelina Jolie, wants to change her name.",
      "content": "Shiloh Jolie-Pitt, the 18-year-old daughter of Brad Pitt and Angelina Jolie, wants to change her name.\r\nAccording to two sources familiar with the matter, the estranged couples daughter filed legal p… [+1537 chars]",
      "url": null,
      "image": "https://media-cldnry.s-nbcnews.com/image/upload/t_nbcnews-fp-1200-630,f_auto,q_auto:best/rockcms/2024-06/240601-split-pitt-shioh-jolie-ch-1045-c8f23b.jpg",
      "publishedAt": "2024-06-01T22:17:09Z",
      "source": {
        "name": "NBC News",
        "url": null,
        "id": "nbc-news"
      }
    }
  ],
  "error": null
}
```

### `GET /top-headlines` #2 - Query top 2 headlines 
####  *Request*
```shell
GET http://localhost:8080/news/api/top-headlines?pageSize=2
```

#### *Response*
```json
{
  "status": "ok",
  "totalArticles": 96,
  "numArticlesReturned": 2,
  "client": "news",
  "articles": [
    {
      "title": "Boeing Starliner spacecraft launch scrubbed minutes before liftoff - The Washington Post",
      "author": "Christian Davenport",
      "description": "Boeing’s Starliner spacecraft is set to make it’s first crewed flight to the International Space Station after multiple delays.",
      "content": "Boeings first attempt to fly its Starliner spacecraft with astronauts on board was scrubbed Saturday less than four minutes before it was to lift off. The delay was called by an automated computer sy… [+3178 chars]",
      "url": null,
      "image": "https://www.washingtonpost.com/wp-apps/imrs.php?src=https://arc-anglerfish-washpost-prod-washpost.s3.amazonaws.com/public/CJRMTH4L326VOHSXQSMNJJQKRI_size-normalized.jpg&w=1440",
      "publishedAt": "2024-06-01T22:24:52Z",
      "source": {
        "name": "The Washington Post",
        "url": null,
        "id": "the-washington-post"
      }
    },
    {
      "title": "Angelina Jolie and Brad Pitt's daughter Shiloh files to drops 'Pitt' from last name - NBC News",
      "author": "Chloe Melas",
      "description": "Shiloh Jolie-Pitt, the 18-year-old daughter of Brad Pitt and Angelina Jolie, wants to change her name.",
      "content": "Shiloh Jolie-Pitt, the 18-year-old daughter of Brad Pitt and Angelina Jolie, wants to change her name.\r\nAccording to two sources familiar with the matter, the estranged couples daughter filed legal p… [+1537 chars]",
      "url": null,
      "image": "https://media-cldnry.s-nbcnews.com/image/upload/t_nbcnews-fp-1200-630,f_auto,q_auto:best/rockcms/2024-06/240601-split-pitt-shioh-jolie-ch-1045-c8f23b.jpg",
      "publishedAt": "2024-06-01T22:17:09Z",
      "source": {
        "name": "NBC News",
        "url": null,
        "id": "nbc-news"
      }
    }
  ],
  "error": null
}
```

### `GET /top-headlines` #3 - Query top 2 headlines, second page
####  *Request*
```shell
GET http://localhost:8080/news/api/top-headlines?pageSize=2&page=2
```

#### *Response*
```json
{
  "status": "ok",
  "totalArticles": 96,
  "numArticlesReturned": 2,
  "client": "news",
  "articles": [
    {
      "title": "UFC 302: Free Fight Marathon - UFC",
      "author": null,
      "description": "Rewatch the full fights of top fighters competing at UFC 302: Makhachev vs Poirier which kicks off with the prelims at 6:30pm ET / 3:30pm PT!Order UFC PPV on...",
      "content": null,
      "url": null,
      "image": "https://i.ytimg.com/vi/ado30o97e6E/maxresdefault.jpg",
      "publishedAt": "2024-06-01T22:13:00Z",
      "source": {
        "name": "YouTube",
        "url": null,
        "id": null
      }
    },
    {
      "title": "Caitlin Clark and Indiana Fever edge Angel Reese and Chicago Sky for first home win, 71-70 - The Associated Press",
      "author": "MICHAEL MAROT",
      "description": "Kelsey Mitchell scored 18 points, NaLyssa Smith added 17 and Caitlin Clark had 11 points, eight rebounds and six assists to help the Indiana Fever win their first home game of the season, 71-70, over the Chicago Sky. Indiana snapped a three-game losing streak…",
      "content": "INDIANAPOLIS (AP) Indiana Fever rookie Caitlin Clark tossed the ball high into the air as time expired Saturday. Veteran guard Kelsey Mitchell just started clapping.\r\nFinally, after four consecutive … [+4642 chars]",
      "url": null,
      "image": "https://dims.apnews.com/dims4/default/d3d3a01/2147483647/strip/true/crop/3000x1688+0+156/resize/1440x810!/quality/90/?url=https%3A%2F%2Fassets.apnews.com%2Fea%2F27%2F3cdbc8468b51029fca43a1fb3ae7%2F32afbe229ad64e24b6f04b977b26f9f7",
      "publishedAt": "2024-06-01T22:03:00Z",
      "source": {
        "name": "Associated Press",
        "url": null,
        "id": "associated-press"
      }
    }
  ],
  "error": null
}
``` 

### `GET /top-headlines` #4 - Query top with keyword support
####  *Request*
```shell
GET http://localhost:8080/news/api/top-headlines?query="UFC"
```

#### *Response*
```json
{
  "status": "ok",
  "totalArticles": 96,
  "numArticlesReturned": 2,
  "client": "news",
  "articles": [
    {
      "title": "UFC 302: Free Fight Marathon - UFC",
      "author": null,
      "description": "Rewatch the full fights of top fighters competing at UFC 302: Makhachev vs Poirier which kicks off with the prelims at 6:30pm ET / 3:30pm PT!Order UFC PPV on...",
      "content": null,
      "url": null,
      "image": "https://i.ytimg.com/vi/ado30o97e6E/maxresdefault.jpg",
      "publishedAt": "2024-06-01T22:13:00Z",
      "source": {
        "name": "YouTube",
        "url": null,
        "id": null
      }
    },
    ...
  ],
  "error": null
}
``` 

