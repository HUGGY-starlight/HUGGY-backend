package com.starlight.huggy.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
abstract class NotFoundException(message: String) : ApiException(message, HttpStatus.NOT_FOUND)
