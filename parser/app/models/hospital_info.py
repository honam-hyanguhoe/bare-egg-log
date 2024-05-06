from sqlalchemy import Column, BigInteger, String
from config.database import Base


class HospitalInfo(Base):
    __tablename__ = 'Hospital'

    hospital_id = Column(BigInteger, primary_key=True, index=True, autoincrement=True)
    sido_code = Column(String(6), nullable=False)
    sido = Column(String(5), nullable=False)
    gungu_code = Column(String(6), nullable=False)
    gungu = Column(String(10), nullable=False)
    dong = Column(String(10), nullable=False)
    zip_code = Column(String(6))
    address = Column(String(255))
    hospital_name = Column(String(50), nullable=False)
    lat = Column(String(20))
    lng = Column(String(20))
    unique_code = Column(String(255), nullable=False)
