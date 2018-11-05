package com.alyona.smail.model

data class ThreadsListResponse(
    val threads: ArrayList<Thread>,
    val nextPageToken: String,
    val resultSizeEstimate: Int
)

data class Thread(
    val id: String,
    val snippet: String,
    val historyId: String
)