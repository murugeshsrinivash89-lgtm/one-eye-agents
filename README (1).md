# ONE EYE CORE

**A multi-agent, fully offline neural assistant for Android that monitors cognitive load, emotional state, and recovery to help users reach and sustain a flow state.**

ONE EYE CORE is not a conventional productivity app. It hosts a neural environment in which four specialized AI agents, each with a distinct role and personality, coordinate through a central event-driven manager. All inference runs on-device, so user data never leaves the phone.

---

## Table of Contents

1. [Overview](#overview)
2. [Multi-Agent Architecture](#multi-agent-architecture)
3. [Agents](#agents)
4. [AI Engine](#ai-engine)
5. [UI Engineering](#ui-engineering)
6. [Technical Demo Flow](#technical-demo-flow)
7. [Tech Stack](#tech-stack)
8. [Getting Started](#getting-started)
9. [Privacy](#privacy)

---

## Overview

ONE EYE CORE observes three dimensions of user state:

- **Logic:** task load and focus discipline
- **Emotion:** mental exhaustion and emotional well-being
- **Recovery:** rest cycles and burnout prevention

Based on this state, the system selects the most appropriate agent to intervene through notifications, conversation, or focus-protocol enforcement.

---

## Multi-Agent Architecture

### BrainManager: Central Coordinator

`BrainManager` is the central hub of the application. It is event-driven rather than polling-based, which keeps background battery consumption minimal.

**Neural Triggers**

| Trigger | Description |
|---|---|
| App Open | Initial state sync on launch |
| Task Completion | Re-evaluates workload and mood |
| Focus Mode Exit | Evaluates session outcome |
| Late-Night Activity | Detects irregular usage timing |

**Processing Pipeline**

1. **Trigger Detection:** an event fires from the system or a user action.
2. **Logic Filtration:** the event is routed through four logic modules, one per agent.
3. **Personality Decision:** using user data persisted in Room DB (fatigue indicators, task overload, usage patterns), `BrainManager` selects the agent best suited to respond and dispatches the notification.

---

## Agents

| Agent | Unit Type | Personality | Responsibility | Visual Identity |
|---|---|---|---|---|
| **ADA** | Logic Unit | Cold, logical, efficient | Alerts the user on task overload | Sharp / square, Tech Blue |
| **ANA** | Resonance Unit | Gentle, supportive, empathetic | Monitors mental exhaustion | Capsule / oval, Soft Pink and Red |
| **RIVER** | Recovery Unit | Calm, balanced, zen | Reminds the user to rest and recover | Circular / fluid, Teal Green |
| **ROO** | Core Guardian | Sassy, playful, strict | Blocks distractions when the focus protocol is violated | Rounded / clay, Neon Blue |

---

## AI Engine

On-device language intelligence is provided by **Google MediaPipe GenAI Tasks**.

- **Model:** `gemma-2b-it-cpu-int4` (quantized for mobile)
- **Runtime:** MediaPipe LLM Inference API
- **Execution:** 100% offline, on-device inference
- **Personality Injection:** when the user starts a conversation, the system injects the selected agent's personality prompt into Gemma before generating a response, so each agent maintains a consistent voice.

Gemma-2b was chosen for its balance of capability and footprint, making private, offline inference practical on mobile hardware.

---

## UI Engineering

The interface follows a cyberpunk heads-up-display (HUD) aesthetic.

### Jetpack Compose

The entire UI is built with declarative Jetpack Compose, enabling smooth state-driven animation and consistent performance.

### Glassmorphism

HUD surfaces use translucent layers, background blur, and neon gradient borders.

### Claymorphism (Agent Character Engine)

- All agents are rendered through custom `Canvas` drawing logic rather than static assets.
- **Responsive scaling:** geometry is computed as a percentage of available screen size instead of hardcoded `dp` values, preventing clipping across device sizes.
- **Animations:** `blinkScale`, `floatOffset`, and `armSwing` are guarded with `isFinite()` checks to ensure stability on Android 15.

---

## Technical Demo Flow

1. **Neural Sync:** on launch, `BrainManager` evaluates triggers and current user state.
2. **Interaction:** the user connects with agents through voice (wake word) or text.
3. **Action:** when Focus Mode is active, the system monitors foreground apps. If the user becomes distracted, ROO is brought on screen to enforce the protocol.
4. **Reflection:** at the end of the day, the user reviews their Operational Score and Emotion Matrix.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, custom Canvas rendering |
| Local Storage | Room Database |
| On-Device AI | MediaPipe GenAI Tasks, Gemma-2b-it-cpu-int4 |
| Architecture | Event-driven, multi-agent |
| Platform | Android (stability-tested on Android 15) |

---

## Getting Started

### Prerequisites

- Android Studio (latest stable)
- A physical Android device or emulator with sufficient RAM for on-device inference
- The `gemma-2b-it-cpu-int4` model file

### Setup

```bash
git clone https://github.com/<your-username>/<your-repo>.git
cd <your-repo>
```

1. Open the project in Android Studio and allow Gradle to sync.
2. Place the Gemma model file in the location expected by the app's model loader.
3. Build and run on a device.

---

## Privacy

ONE EYE CORE is designed to be privacy-first:

- All conversations and neural logs are stored locally.
- All AI inference is performed on-device.
- No user data is transmitted to any cloud service.

---

## Summary

A hybrid of Jetpack Compose visual engineering and MediaPipe on-device AI, delivering an adaptive assistant experience that optimizes human performance through specialized neural agents.
