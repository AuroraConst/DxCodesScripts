# PDF Scripts Usage Guide

## Project Structure
- `project.scala` - Contains all dependencies for the project
- `openpdfzio.sc` - Contains the PdfService trait and implementation
- `example_usage.sc` - Contains examples of how to use PdfService

## Running the Examples

### Option 1: Run with Scala CLI (Recommended)
```cmd
# Run the basic example
scala-cli run example_usage.sc --main-class PdfServiceExample

# Run the invoice example  
scala-cli run example_usage.sc --main-class FunctionalPdfExample

# Run the safe example with error handling
scala-cli run example_usage.sc --main-class SafePdfExample
```

### Option 2: Run all files together
```cmd
# This will compile all .sc files in the directory together
scala-cli run . --main-class PdfServiceExample
```

### Option 3: Compile and run
```cmd
# Compile first
scala-cli compile .

# Then run
scala-cli run . --main-class PdfServiceExample
```

## How It Works

1. **Dependencies**: All dependencies are centralized in `project.scala`
2. **Automatic Import**: Scala CLI automatically compiles all `.sc` files in the same directory together
3. **PdfService Usage**: The `PdfService` trait and `PdfServiceLive` implementation from `openpdfzio.sc` are available in `example_usage.sc`
4. **ZIO Layer**: The service is provided via `PdfService.live` layer

## Output
Running any of the examples will create an `output.pdf` file in the current directory.

## Key Points
- No explicit import of `openpdfzio.sc` needed - Scala CLI handles this automatically
- All using directives are centralized in `project.scala` to avoid conflicts
- The PdfService uses ZIO for effect management and resource safety
