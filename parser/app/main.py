from fastapi import FastAPI, HTTPException
from utils.hospital_parsing import parse_and_insert_excel_data

app = FastAPI()

@app.get("/upload_excel/")
async def upload_excel():
    file_path="files/hospital_info_2309.xlsx"
    try:
        parse_and_insert_excel_data(file_path)
        return {"message": "Data successfully inserted into the database"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

