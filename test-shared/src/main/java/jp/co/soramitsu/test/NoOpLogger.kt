package jp.co.soramitsu.test

import jp.co.soramitsu.fearless_utils.wsrpc.logging.Logger


object NoOpLogger : Logger {
    override fun log(message: String?) {
        // pass
    }

    override fun log(throwable: Throwable?) {
        // pass
    }
}