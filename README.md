# github-browser
A simple Android app to browse public GitHub repositories.

GitHub Browser app uses the GitHub REST API (https://api.github.com/) to retrieve basic information about
selected public repositories.

Repositories are grouped by language. Languages with the most repositories are listed first. 
Within each language group, repositories are sorted in descending order by the stargazers count.

Architecture: MVVM using Android architecture components

Third-Party libraries used:

- Retrofit
- Gson
