export interface IPythonResult {
  id?: number;
  requestHash?: string;
  input?: string;
  output?: number;
}

export const defaultValue: Readonly<IPythonResult> = {};
