//
//  Flow.swift
//  iosApp
//
//  Created by Levi Bostian on 10/18/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import KMPNativeCoroutinesCombine
import KMPNativeCoroutinesCore
import Combine

/*
 Kotlin coroutine's Flow is easy to use in Android, but not as easy to use in iOS. There are many solutions out there, but in this project, we have decided to convert Flow into iOS Combine Publisher because the platform has native support for that. By using Publisher, we get all the features that come with it.
 
 So, an iOS ViewModel will create a Publisher from a Coroutine Flow which is housed inside of the shared/common ViewModel codebase.
 
 To make the conversion to Publisher, we currently use a tool: https://github.com/rickclephas/KMP-NativeCoroutines because the tool generates some extensions for us to make the conversion easier.
 We could do it manually as outlined in articles like this: https://betterprogramming.pub/using-kotlin-flow-in-swift-3e7b53f559b6 (https://web.archive.org/web/20221018180442/https://betterprogramming.pub/using-kotlin-flow-in-swift-3e7b53f559b6)
 but this tool is lightweight, gets updated with Kotlin updates, and doesn't have many drawbacks in migrations if we move away from it.
 
 To make migrations and upgrading easier in the codebase, this file exists as an "Alias" file. We create typealias, duplicate functions, etc in this file that the Swift codebase uses. Our goal is to *not* have to import a library into
 any of our Swift source code. Instead, all of the Swift source code simply calls a function to do the conversion from Flow to Publisher, then it just does work against that Publisher just like any other iOS code would.
 */
func createPublisher<Output, Failure: Error, Unit>(
    for nativeFlow: @escaping NativeFlow<Output, Failure, Unit>
) -> AnyPublisher<Output, Failure> {
    return KMPNativeCoroutinesCombine.createPublisher(for: nativeFlow)
}
