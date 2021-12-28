# v0.0.4
- Fixed annotation checking method

# v0.0.3
- Event Based client packet read.
- Fixed AddressUtil Bug

# v0.0.2-Beta

## 2021-10-17
- Fixed @SonetConstruct bug

# v0.0.1-Beta
## 2021-08-06
- Started project as RemoteActions

## 2021-08-09
- Created git repository

## 2021-08-10
- Created most of the base features

## 2021-08-13
- Updated core features

## 2021-08-15
- Updated the main logic of connection
- Renamed project to Sonet

## 2021-08-16
- Added SonetBuffer

## 2021-08-17
- Usage of nio and buffer
- Rename all source code name to RemoteActions -> Sonet

## 2021-08-18
- Update all package trees
- GNU License version 3
- Update README
- Simpler and faster deserialization

## 2021-08-23
- Added async support for server
- Better PacketDeserializing
- Enhanced abstract SonetPacket class
- Added use for annotation @SonetDeserialize

## 2021-08-31
- Created a selector for the client
- Any method name allowed for the packet with @SonetDeserialize
- Auto infer the type of buffer element
- AbstractClass SonetPacket -> Interface SonetPacket
- Created @SonetData annotation. If attached to a field in a SonetPacket class, SonetPacket automatically searches for the field and serialize it.

# v0.0.1-RC
## 2021-09-01
- Fixed a major bug related to SonetBuffer. It was due to wrong memory allocation
- Removed all debug statements

## 2021-09-08
- PacketDeserializer -> SonetDataDeserializer & SonetBufferType -> SonetDataType
- Upgraded package structure
- Fixed SonetDataType's type inferring system bug due to primitive class checking
- Added SonetDataContainer, an extensive object SonetData
- @SonetConstruct
- SonetPacket and SonetDataContainer inherits AbstractSonetData
- PacketRegistry -> classname serialization -> Header to 4 bytes
- Better SonetBuffer

# v0.0.1

## 2021-10-17
- StaticSonetBuffer to fix the problem of excess heap allocation
- Fixed a bug that prevented proper BufferIO with sockets
- Fixed a bug that Packets couldn't be deserialized
- Changed SonetBuffer's String read & write system from chars to bytes
- When Server accepts packets, a specific PacketType can be declared through Generics
- Removed all debug statements
- Stabilized everything and ready for the first release