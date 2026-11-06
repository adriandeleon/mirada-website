// The Mirada release version is generated from the app's pom.xml, at the newest release tag, into
// version.ts by scripts/GenCommands.java and committed, so the build never reads the Mirada
// repository (it isn't checked out in CI). Re-exported here so pages import everything from one place.
import { version } from "./version";

export { version };

export const repo = "https://github.com/adriandeleon/Mirada";
export const latestRelease = `${repo}/releases/latest`;
