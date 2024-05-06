import pandas as pd
from config.database import SessionLocal
from models.hospital_info import HospitalInfo


def map_excel_row_to_hospital_info(row):
    return HospitalInfo(
        sido_code=row['시도코드'],
        sido=row['시도코드명'],
        gungu_code=row['시군구코드'],
        gungu=row['시군구코드명'],
        dong=row['읍면동'] if pd.notna(row['읍면동']) else '',
        zip_code=row['우편번호'] if pd.notna(row['우편번호']) else '',
        address=row['주소'] if pd.notna(row['주소']) else '',
        hospital_name=row['요양기관명'],
        lat=row['좌표(Y)'] if pd.notna(row['좌표(Y)']) else None,
        lng=row['좌표(X)'] if pd.notna(row['좌표(X)']) else None
    )


def parse_and_insert_excel_data(file_path):
    df = pd.read_excel(file_path)
    session = SessionLocal()
    try:
        for index, row in df.iterrows():
            hospital_info = map_excel_row_to_hospital_info(row)
            session.add(hospital_info)
        session.commit()
    except Exception as e:
        session.rollback()
        raise
    finally:
        session.close()
