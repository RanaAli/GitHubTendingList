package com.rana.domain.usecases

import javax.inject.Inject


abstract class UseCase {
    abstract suspend operator fun invoke(): Any
}