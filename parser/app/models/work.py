

from config.database import Base


class Work(Base):
    __tablename__ = 'Work'

    work_id = Column(BigInteger, primary_key=True, index=True, autoincrement=True)
    work_uuid = Column(String(255), nullable=False)
    work_date = Column(String(255), nullable=False)