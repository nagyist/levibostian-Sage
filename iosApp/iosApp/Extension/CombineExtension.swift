//
//  Combine.swift
//  iosApp
//
//  Created by Levi Bostian on 10/19/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Combine

extension AnyPublisher {
    func sinkMain(onComplete completeCallback: ((Subscribers.Completion<Failure>) -> Void)?, receiveValue receiveCallback: @escaping (Output) -> Void) -> AnyCancellable {
        return self
            .receive(on: DispatchQueue.main)
            .sink { completion in
                completeCallback?(completion)
            } receiveValue: { value in
                receiveCallback(value)
            }
    }
    
    func sinkMainReceive(_ receiveCallback: @escaping (Output) -> Void) -> AnyCancellable {
        return sinkMain(onComplete: nil, receiveValue: receiveCallback)
    }

}
