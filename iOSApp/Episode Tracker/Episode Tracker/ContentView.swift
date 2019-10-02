//
//  ContentView.swift
//  Episode Tracker
//
//  Created by Yury Polek on 9/30/19.
//  Copyright © 2019 Yury Polek. All rights reserved.
//

import SwiftUI
import SharedCode

struct ContentView: View {
    @State private var selection = 0
 
    var body: some View {
        TabView(selection: $selection){
            Text(CommonKt.createApplicationScreenMessage())
                .font(.title)
                .tabItem {
                    VStack {
                        Image("first")
                        Text("First")
                    }
                }
                .tag(0)
            Text("Second View")
                .font(.title)
                .tabItem {
                    VStack {
                        Image("second")
                        Text("Second")
                    }
                }
                .tag(1)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
