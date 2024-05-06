from fastapi import FastAPI, HTTPException
from utils.hospital_parsing import parse_and_insert_excel_data,fetch_hospital_save_to_json

app = FastAPI()

@app.get("/upload-excel/")
async def upload_excel():
    file_path="files/hospital_info_2309.xlsx"
    try:
        parse_and_insert_excel_data(file_path)
        return {"message": "Data successfully inserted into the database"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/fetch-and-save/")
async def fetch_and_save():
    try:
        await fetch_hospital_save_to_json()
        return {"message": "Data fetched and saved successfully!"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
