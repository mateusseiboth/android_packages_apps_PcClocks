#!/bin/bash
# SPDX-FileCopyrightText: The LineageOS Project
# SPDX-License-Identifier: Apache-2.0
#
# Runs the host tests of the clock model without a build, with the tree's Kotlin compiler and
# JDK and the JUnit jars a previous build left in out/. `atest PcClocksHostTests` runs the same
# tests through Soong.
set -euo pipefail

HERE=$(cd "$(dirname "$0")" && pwd)
REPO=$(dirname "$HERE")
TOP=${ANDROID_BUILD_TOP:-$(cd "$REPO/../../.." && pwd)}
OUT=$(mktemp -d)
trap 'rm -rf "$OUT"' EXIT

export JAVA_HOME=$TOP/prebuilts/jdk/jdk21/linux-x86
export JAVA_OPTS=-Xmx1g
INTERMEDIATES=$TOP/out/soong/.intermediates/external
JUNIT=$INTERMEDIATES/junit/junit/linux_glibc_common/javac/junit.jar
HAMCREST=$INTERMEDIATES/hamcrest/hamcrest-core/hamcrest/linux_glibc_common/javac/hamcrest.jar
STDLIB=$TOP/external/kotlinc/lib/kotlin-stdlib.jar

mapfile -t SOURCES < <(find "$REPO/lib/src/org/lineageos/pcclocks/model" "$HERE/host/src" -name '*.kt')
"$TOP/external/kotlinc/bin/kotlinc" -nowarn -jvm-target 21 -cp "$JUNIT" -d "$OUT" "${SOURCES[@]}"

mapfile -t TESTS < <(cd "$HERE/host/src" && find . -name '*Test.kt' | sed 's|^\./||; s|\.kt$||; s|/|.|g')
"$JAVA_HOME/bin/java" -Xmx512m -cp "$OUT:$JUNIT:$HAMCREST:$STDLIB" org.junit.runner.JUnitCore "${TESTS[@]}"
