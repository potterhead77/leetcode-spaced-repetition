package com.nandan.spaced_repetition.graphql;

public class GraphQlQueries {

    public static final String FETCH_QUESTIONS_QUERY = """
            query problemsetQuestionListV2(
              $filters: QuestionFilterInput,
              $limit: Int,
              $searchKeyword: String,
              $skip: Int,
              $sortBy: QuestionSortByInput,
              $categorySlug: String
            ) {
              problemsetQuestionListV2(
                filters: $filters
                limit: $limit
                searchKeyword: $searchKeyword
                skip: $skip
                sortBy: $sortBy
                categorySlug: $categorySlug
              ) {
                questions {
                  id
                  titleSlug
                  title
                  translatedTitle
                  difficulty
                  topicTags {
                    name
                    slug
                  }
                  acRate
                  contestPoint
                }
                totalLength
              }
            }
            """;




    public static final String FETCH_USER_RECENT_SUBMISSIONS = """
           query recentAcSubmissions($username: String!, $limit: Int!) {
              recentAcSubmissionList(username: $username, limit: $limit) {
                id
                title
                titleSlug
                timestamp
                lang
                time
                url
                statusDisplay
              }
            }
           """;


}
