# MythicWorldLib

## What's it?
This is a shared library for my Mythic World mods. Its code is extracted from MythicWorldTweaks for reuse convenience.
## What does it do?
It doesn't change any gameplay experience on its own, it only provides the following frameworks:
- Payload-based networking interfaces.
- Reflection utils for manipulating Minecraft fields.
- A fix for Mojang's RecordCodecBuilder to maintain JSON ordering when serializing.
- A simple util to determine whether the mod is running on a client or dedicated server.
## How to use it?
Simply place it beside my other mods that depend on it.