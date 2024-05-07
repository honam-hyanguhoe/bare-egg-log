import pandas as pd
from sqlalchemy import select
import json
import aiofiles

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
            # existing_hospital = session.query(HospitalInfo).filter_by(unique_code=hospital_info.unique_code).first()
            # if existing_hospital:
                # 기존 데이터가 있으면 업데이트
                # existing_hospital.update(**hospital_info.__dict__)
            # else:
                # 없으면 새로 추가
                # session.add(hospital_info)
        session.commit()
    except Exception as e:
        session.rollback()
        raise
    finally:
        session.close()


# HospitalInfo 객체를 사전으로 변환하는 함수
def convert_to_dict(hospital_info):
    return {
        "hospitalId": hospital_info.hospital_id,
        "sidoCode": hospital_info.sido_code,
        "sido": hospital_info.sido,
        "gunguCode": hospital_info.gungu_code,
        "gungu": hospital_info.gungu,
        "dong": hospital_info.dong,
        "zipCode": hospital_info.zip_code,
        "address": hospital_info.address,
        "hospitalName": hospital_info.hospital_name,
        "lat": hospital_info.lat,
        "lng": hospital_info.lng,
    }


async def fetch_hospital_save_to_json():
    # 데이터베이스에서 데이터 조회
    try:
        session = SessionLocal()
        result = session.execute(select(HospitalInfo)).all()
        users_data = [convert_to_dict(row[0]) for row in result]
        print(users_data[0])

        # JSON 파일로 비동기적으로 저장
        async with aiofiles.open('files/hospital_202309.json', 'w', encoding='utf-8') as file:
            await file.write(json.dumps(users_data, indent=4, ensure_ascii=False))
    except Exception as e:
        print("fail")
        raise
    finally:
        session.close()
