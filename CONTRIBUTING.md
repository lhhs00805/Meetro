# Contributing to Meetro

Thank you for your interest in Meetro! 🚇
Everyone is welcome to contribute, even if you have never contributed to open source before.

Please read our [Code of Conduct](CODE_OF_CONDUCT.md) before you start.

---

## Ways to contribute

| What you can do | Need Java? | Good first contribution? |
| --- | --- | --- |
| Add or update **facility data** for a station | No | ✅ Yes |
| Add a **subway line** | No | ✅ Yes |
| Report a **bug** or suggest an idea | No | ✅ Yes |
| Improve **documentation** (README, this guide) | No | ✅ Yes |
| Fix a bug or add a **feature** | Yes | After your first PR |

---

## How we work

We follow this flow for every change:

```
Issue → Branch → Commit → Pull Request → Review → Merge
```

### 1. Start with an issue

- Look at the [Issues](../../issues) tab first. Issues labeled `good first issue` are a great place to start.
- If you want to work on an issue, leave a comment like *"I'd like to work on this."* so others know.
- If your idea or bug is not listed, open a new issue and describe it.

### 2. Create a branch

Do not work directly on the `main` branch. Create a new branch with one of these names:

| Type | Branch name example |
| --- | --- |
| New feature | `feature/purpose-filter` |
| Bug fix | `fix/station-name-input` |
| Data | `data/hongik-univ-facilities` |
| Documentation | `docs/update-readme` |

### 3. Commit your changes

- Keep each commit small and focused on one thing.
- Write commit messages in English, starting with a verb:
  - ✅ `Add facility data for Hongik Univ.`
  - ✅ `Fix BFS distance for circular lines`
  - ❌ `update`, `asdf`, `final final`

### 4. Open a pull request (PR)

- Fill in the PR template.
- Link the issue by writing `Closes #issue-number` in the PR description.
- Keep each PR small. One PR should solve one issue.

### 5. Review

- Every PR needs **at least 1 approval** from another team member before it is merged.
- You cannot approve your own PR.
- Reviewers should check: Does it work? Is it easy to understand? Are there tests?
- Be kind and specific in reviews. Suggest, don't command. ("Could we rename this to...?")
- After the review, update your PR and reply to each comment.

### 6. Merge

- The PR author merges after approval and after all tests pass.
- Delete the branch after merging.

---

## Adding facility data (no Java needed!)

This is the easiest way to contribute. Facility files are in `data/facilities/`.

### Step by step

1. Copy an existing file, for example `data/facilities/hongik-univ.json`.
2. Rename it using the station's English name in lowercase with hyphens (e.g. `gangnam.json`).
3. Fill in the information:

```json
{
  "station": "Gangnam",
  "updated": "2026-11",
  "facilities": { "food": 5, "cafe": 5, "movie": 3, "karaoke": 4, "study": 4, "walk": 2 },
  "places": [
    { "name": "Example Café", "type": "cafe" },
    { "name": "Example Restaurant", "type": "food" }
  ]
}
```

4. Open a PR with the title `Add facility data for <station name>`.

### How to score facilities (0–5)

Count places within about a **10-minute walk** from the station.

| Score | Meaning |
| --- | --- |
| 0 | None |
| 1 | 1–2 places |
| 2 | A few places |
| 3 | Many places, easy to find one |
| 4 | Lots of places, popular area for this purpose |
| 5 | One of the best areas in Seoul for this purpose |

### Rules for facility data

- Use the purpose keys exactly: `food`, `cafe`, `movie`, `karaoke`, `study`, `walk`.
- Add 2–3 places for each purpose with a score of 1 or more, if you can.
- Update `"updated"` to the year and month you checked (e.g. `"2026-11"`).
- Only add information you checked yourself. **Do not copy text, reviews, or photos from map services or other websites.**

---

## Adding a subway line

Line files are in `data/lines/`.

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

- List stations **in order**. Stations next to each other are connected.
- Set `"circular": true` only if the line is a loop (like Line 2).
- A branch line (a line that splits off) should be a separate file.
- Use the **same station name** in every file. A transfer station is recognized when two lines use the same name.
- If you use public data, write the source in your PR and in the README's **Data sources** section.

---

## Contributing code

- **Language**: Java 17 or later
- **Build tool**: Maven 3.9 or later (dependencies are listed in `pom.xml`)
- Put code in `src/main/java` and tests in `src/test/java`.
- Write **JUnit tests** for new features and bug fixes.
- Run all tests before opening a PR:

```bash
mvn test
```

- Use clear names for classes and methods (e.g. `MeetingPointFinder`, `distancesFrom`).
- Add short comments where the logic is not obvious.

---

## Using AI tools

AI tools are allowed in this project. If you used AI for your contribution, please write in your PR:

1. **What you asked the AI to do**
2. **How you checked the result** (tests, manual check, review)
3. **What you accepted, changed, or rejected**

You are responsible for every line you submit. Please make sure you understand your changes and can explain them during review.

---

## Questions?

Open an issue with the `question` label, or contact the maintainers:
[@lhhs00805](https://github.com/lhhs00805), [@yoomyeongju0113](https://github.com/yoomyeongju0113), [@jeonhajinn](https://github.com/jeonhajinn)

Thank you for helping groups find a fair place to meet! 🙌

---

## Questions?

Open an issue with the `question` label, or contact the maintainers:
[@lhhs00805](https://github.com/lhhs00805), [@yoomyeongju0113](https://github.com/yoomyeongju0113), [@jeonhajinn](https://github.com/jeonhajinn)

Thank you for helping groups find a fair place to meet! 🙌
