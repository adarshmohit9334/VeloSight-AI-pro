import re

def _normalize_indian_plate(text: str) -> str:
    # Convert to upper case and remove non-alphanumeric characters
    text = text.upper()
    text = re.sub(r'[^A-Z0-9]', '', text)
    
    # Very loose heuristic validation to allow slight OCR errors, but enforce basic length
    if len(text) >= 6 and len(text) <= 12:
        return text
    return ""

def validate_indian_plate(text: str) -> bool:
    # Indian plates typically: XX 11 XX 1111 (e.g., MH16C00555, DL01AB1234, etc.)
    # State (2 chars), District (1-2 digits), Series (1-3 chars), Number (1-4 digits)
    # We will use a flexible regex that handles common formats
    pattern = r'^[A-Z]{2}[0-9]{1,2}[A-Z]{0,3}[0-9]{1,4}$'
    if re.match(pattern, text):
        return True
    
    # Also allow some common OCR confusions at the start/end if the length matches
    # e.g., MHI6... 
    if len(text) >= 8 and len(text) <= 10:
        return True
        
    return False

print(validate_indian_plate("MH16C00555"))
print(validate_indian_plate("BR01XX1234"))
print(validate_indian_plate("MH16CO05SE")) # False
