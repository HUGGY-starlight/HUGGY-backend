package com.starlight.huggy.auth.exception

import com.starlight.huggy.common.exception.ForbiddenException


class InvalidAccessException : ForbiddenException {
    constructor(message: String) : super(message)
    constructor() : super("해당 리소스에 대한 권한이 없습니다.")
}
