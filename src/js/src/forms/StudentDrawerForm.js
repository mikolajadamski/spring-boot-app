import React, { useState } from 'react';
import {Drawer, Input, Col, Select, Form, Row, Button, Spin} from 'antd';
import { addNewStudent } from '../client';
import { LoadingOutlined } from '@ant-design/icons';
import {successNotification, errorNotification } from '../Notifications';

const {Option} = Select;


export function StudentDrawerForm({showDrawer, setShowDrawer, fetchStudents}) {

    const onClose = () => setShowDrawer(false);
    const [submitting, setSubmitting] = useState(false);
    const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;

    const onFinish = student => {
        setSubmitting(true)
        addNewStudent(student)
        .then(() => {
            console.log("student added");
            onClose();
            successNotification("Student added", `${student.name} was added` )
            fetchStudents();
        })
        .catch(error => {
            console.log(error)
        })
        .finally(() => {
            setSubmitting(false)
        })
        
    };

    const onFinishFailed = errorInfo => {
        console.log(JSON.stringify(errorInfo, null, 2));
    };

    return <Drawer
        title="Create new student"
        width={720}
        onClose={onClose}
        visible={showDrawer}
        bodyStyle={{paddingBottom: 80}}
        footer={
            <div
                style={{
                    textAlign: 'right',
                }}
            >
                <Button onClick={onClose} style={{marginRight: 8}}>
                    Cancel
                </Button>
            </div>
        }
    >
        <Form layout="vertical"
              onFinishFailed={onFinishFailed}
              onFinish={onFinish}
              hideRequiredMark>
            <Row gutter={16}>
                <Col span={12}>
                    <Form.Item
                        name="firstName"
                        label="First Name"
                        rules={[{required: true, message: 'Please enter student first name'}]}
                    >
                        <Input placeholder="Please enter student first name"/>
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item
                        name="lastName"
                        label="Last Name"
                        rules={[{required: true, message: 'Please enter student last name'}]}
                    >
                        <Input placeholder="Please enter student last name"/>
                    </Form.Item>
                </Col>
            </Row>
            <Row gutter={16}>
            <Col span={12}>
                    <Form.Item
                        name="email"
                        label="Email"
                        rules={[{required: true, message: 'Please enter student email'}]}
                    >
                        <Input placeholder="Please enter student email"/>
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item
                        name="gender"
                        label="gender"
                        rules={[{required: true, message: 'Please select a gender'}]}
                    >
                        <Select placeholder="Please select a gender">
                            <Option value="MALE">MALE</Option>
                            <Option value="FEMALE">FEMALE</Option>
                            <Option value="OTHER">OTHER</Option>
                        </Select>
                    </Form.Item>
                </Col>
            </Row>
            <Row>
                <Col span={12}>
                    <Form.Item >
                        <Button type="primary" htmlType="submit">
                            Submit
                        </Button>
                    </Form.Item>
                </Col>
            </Row>
            <Row>
                {submitting && <Spin indicator={antIcon} />}
            </Row>
        </Form>
    </Drawer>
}