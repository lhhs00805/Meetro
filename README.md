# Meetro 🚇

**Meet + Metro.** Meetro finds a fair subway station for your group, based on everyone's travel time and what you want to do there.

> 🚧 **Work in progress.** Meetro is being built as a team project for the Open Source Software course at Seoul National University of Science and Technology (Fall 2026). Some features below are planned and not finished yet.

## Why Meetro?

When friends meet, someone usually has to travel much farther than everyone else. Even if you find the station that is halfway by distance, there is often nothing to do there. So the group goes back to the same familiar places, and the same person travels far every time.

Meetro solves this by looking at **two things at once**:

1. **Fairness**: how long each person needs to travel
2. **Purpose**: whether the station has something to do for your plan (food, café, movie, karaoke, study, or a walk)

## Who is it for?

University students and friend groups in Seoul who meet often but find it hard to choose a place that is fair for everyone.

## How it works

1. Each person enters their starting station.
2. The group chooses what they want to do today: **food, café, movie, karaoke, study, or walk**.
3. Meetro calculates how many stops each person needs to travel to every station, using **BFS (Breadth-First Search)** on the subway map.
4. Stations with no place for your purpose are removed.
5. The remaining stations are scored:

   ```
   total score = distance score × weight + facility score × weight
   ```

   - **Distance score**: higher when the person who travels the longest still arrives quickly. You can also choose the smallest total travel time instead.
   - **Facility score**: higher when the station has more places for your purpose.
6. Meetro shows the **top 3 stations**, how long each person needs to travel, 2–3 nearby places, and a map search link.

## Example output

```
Purpose: Café
Rank 1. Euljiro 3(il)-ga   (score 81)
   - Minsu  (Gangnam):        6 stops, about 12 min
   - Jiwon  (Hongik Univ.):   7 stops, about 14 min
   - Myeongju (Seoul Nat'l Univ. of Sci. & Tech.): 8 stops, about 16 min
   Nearby: Example Café A, Example Café B
   Map: https://map.naver.com/... (search: "Euljiro 3-ga café")
```

> Travel time is an estimate (about 2 minutes per stop). It may be different from the real travel time.

## Features

| Status | Feature |
| --- | --- |
| 🔨 In progress | Find the fairest station from each person's starting station (BFS) |
| 🔨 In progress | Purpose-based recommendation (food, café, movie, karaoke, study, walk) |
| 🔨 In progress | Show nearby places and map search links |
| 📝 Planned | Personal travel conditions (e.g. "at most 1 transfer", "stations with elevators only") |
| 📝 Planned | Fairness history: give priority to people who traveled far last time |
| 📝 Planned | Return-home deadline and departure time for each person |
| 📝 Planned | Total cost (transport fare + expected spending) |

## Data

All data is stored as JSON files, so you can contribute **without writing Java code**.

```
data/
├── lines/         subway lines and stations
└── facilities/    places around each station
```

**Line file** (`data/lines/line2.json`)

```json
{
  "line": "Line 2",
  "circular": true,
  "stations": [
    { "name": "City Hall", "elevator": true },
    { "name": "Euljiro 1(il)-ga", "elevator": true }
  ]
}
```

Stations listed next to each other are connected. If `circular` is `true`, the last station is also connected to the first one.

**Facility file** (`data/facilities/hongik-univ.json`)

```json
{
  "station": "Hongik Univ.",
  "updated": "2026-11",
  "facilities": { "food": 5, "cafe": 5, "movie": 2, "karaoke": 4, "study": 3, "walk": 3 },
  "places": [
    { "name": "Example Café", "type": "cafe" }
  ]
}
```

Each facility score is from **0** (none) to **5** (a lot).

## Getting started

> These steps will work once the first version is ready.

**Requirements**: Java 17 or later

```bash
git clone https://github.com/lhhs00805/meetro.git
cd meetro
./gradlew run      # run Meetro
./gradlew test     # run tests
```

## Contributing

We welcome contributions! The easiest way to start is to **add or update facility data for a station you know well**.
Please read [CONTRIBUTING.md](CONTRIBUTING.md) and our [Code of Conduct](CODE_OF_CONDUCT.md) before you open a pull request.

## Use of AI tools

We use AI tools during development. For each feature, we record what we asked the AI to do, how we checked the result, and what we accepted, changed, or rejected. Every change is reviewed by a team member before it is merged.

## Data sources

- Station data: _to be added (we will list every public data source we use)_
- Facility scores: collected and rated by the Meetro team and contributors

## License

Meetro is released under the [MIT License](LICENSE).

We chose the MIT License because it is short, easy to understand, and widely used. It lets anyone use, change, and share Meetro freely, as long as they keep our copyright notice. Since our goal is to make it easy for other students to contribute and reuse our code and station data, a simple permissive license fits our project best.

Data taken from public sources follows the terms of its original source.

## Team: Hello Java ☕

| Name | GitHub | Role |
| --- | --- | --- |
| Yoo Myeongju | [@yoomyeongju0113](https://github.com/yoomyeongju0113) | _TBD_ |
| Lim Hyunseo | [@lhhs00805](https://github.com/lhhs00805) | _TBD_ |
| Jeon Hajin | [@jeonhajinn](https://github.com/jeonhajinn) | _TBD_ |
