# Sonet
#### A simple implementation of a packet-exchanging lightweight tcp socket

### Examples
[ClientExample]("https://github.com/dolphin2410/sonet/tree/master/core/src/main/java/io/github/teamcheeze/sonet/sample/ClientApplication.java")


[ServerExample]("https://github.com/dolphin2410/sonet/tree/master/core/src/main/java/io/github/teamcheeze/sonet/sample/ServerApplication.java")


[SamplePacket]("https://github.com/dolphin2410/sonet/tree/master/core/src/main/java/io/github/teamcheeze/sonet/sample/SamplePacket.java")

## TODOs
- C̶r̶e̶a̶t̶e̶ ̶a̶ ̶w̶a̶y̶ ̶f̶o̶r̶ ̶t̶h̶e̶ ̶s̶e̶r̶v̶e̶r̶ ̶t̶o̶ ̶c̶o̶m̶m̶u̶n̶i̶c̶a̶t̶e̶ ̶t̶o̶ ̶t̶h̶e̶ ̶c̶l̶i̶e̶n̶t̶ ̶a̶t̶ ̶a̶n̶y̶ ̶t̶i̶m̶e̶
- O̶p̶t̶i̶m̶i̶z̶e̶ ̶t̶h̶e̶ ̶p̶a̶c̶k̶a̶g̶e̶ ̶t̶r̶e̶e̶
- Create a realistic way to use this project
- M̶a̶k̶e̶ ̶a̶ ̶p̶a̶c̶k̶e̶t̶ ̶l̶i̶s̶t̶e̶n̶e̶r̶ ̶s̶e̶p̶a̶r̶a̶t̶e̶l̶y̶ ̶f̶o̶r̶ ̶e̶a̶c̶h̶ ̶s̶e̶r̶v̶e̶r̶ ̶i̶n̶s̶t̶a̶n̶c̶e̶
- M̶a̶k̶e̶ ̶a̶ ̶w̶a̶y̶ ̶t̶o̶ ̶p̶r̶e̶v̶e̶n̶t̶ ̶t̶h̶e̶ ̶c̶l̶i̶e̶n̶t̶ ̶f̶r̶o̶m̶ ̶a̶c̶c̶e̶s̶s̶i̶n̶g̶ ̶s̶e̶r̶v̶e̶r̶-̶r̶e̶l̶a̶t̶e̶d̶ ̶m̶e̶t̶h̶o̶d̶s̶
- C̶o̶n̶t̶i̶n̶u̶e̶ ̶t̶o̶ ̶r̶e̶s̶e̶a̶r̶c̶h̶ ̶a̶ ̶w̶a̶y̶ ̶f̶o̶r̶ ̶a̶ ̶f̶a̶s̶t̶e̶r̶,̶ ̶m̶o̶r̶e̶ ̶s̶e̶c̶u̶r̶e̶,̶ ̶a̶n̶d̶ ̶m̶o̶r̶e̶ ̶l̶i̶g̶h̶t̶w̶e̶i̶g̶h̶t̶ ̶w̶a̶y̶ ̶f̶o̶r̶ ̶s̶o̶c̶k̶e̶t̶ ̶c̶o̶n̶n̶e̶c̶t̶i̶o̶n̶s̶
- A̶d̶d̶ ̶a̶ ̶b̶e̶t̶t̶e̶r̶ ̶w̶a̶y̶ ̶o̶f̶ ̶r̶e̶g̶i̶s̶t̶e̶r̶i̶n̶g̶ ̶a̶ ̶p̶a̶c̶k̶e̶t̶
- A̶n̶ ̶a̶u̶t̶o̶m̶a̶t̶i̶c̶ ̶w̶a̶y̶ ̶f̶o̶r̶ ̶t̶h̶e̶ ̶S̶o̶n̶e̶t̶B̶u̶f̶f̶e̶r̶ ̶t̶o̶ ̶c̶o̶n̶v̶e̶r̶t̶ ̶B̶y̶t̶e̶A̶r̶r̶a̶y̶O̶u̶t̶p̶u̶t̶S̶t̶r̶e̶a̶m̶ ̶t̶o̶ ̶B̶y̶t̶e̶B̶u̶f̶f̶e̶r̶
- B̶e̶t̶t̶e̶r̶ ̶N̶I̶O̶ ̶S̶o̶c̶k̶e̶t̶C̶h̶a̶n̶n̶e̶l̶ ̶l̶o̶g̶i̶c̶

## Change logs
- 2021-08-06 Started project as RemoteActions
- 2021-08-09 Created git repository
- 2021-08-10 Created most of the base features
- 2021-08-13 Updated core features
- 2021-08-15 Updated the main logic of connection
- 2021-08-15 Renamed project to Sonet
- 2021-08-16 Added SonetBuffer
- 2021-08-17 Usage of nio and buffer
- 2021-08-17 Rename all source code name to RemoteActions -> Sonet
- 2021-08-18 Update all package trees
- 2021-08-18 GNU License version 3
- 2021-08-18 Update README
- 2021-08-18 Simpler and faster deserialization
- 2021-08-23 Added async support for server
- 2021-08-23 Better PacketDeserializing
- 2021-08-23 Enhanced abstract SonetPacket class
- 2021-08-23 Added use for annotation @SonetDeserialize
