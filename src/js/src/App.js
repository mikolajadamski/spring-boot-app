import React, {useState, useEffect } from 'react';
import Container from './Container';
import './App.css';
import {getAllStudents, deleteStudent} from './client';
import { errorNotification, successNotification } from './Notifications';
import { StudentDrawerForm } from './forms/StudentDrawerForm';
import { LoadingOutlined, PlusOutlined} from '@ant-design/icons';
import { 
  Layout, 
  Menu, 
  Breadcrumb,
  Table,
  Spin, 
  Empty,
  Button,
  Badge,
  Tag,
  Radio,
  Popconfirm} from 'antd';
import {
  DesktopOutlined,
  PieChartOutlined,
  FileOutlined,
  TeamOutlined,
  UserOutlined,
} from '@ant-design/icons';

const { Header, Content, Footer, Sider} = Layout;
const { SubMenu } = Menu;

const removeStudent = (studentId, callback) => {
  deleteStudent(studentId).then(()=>{
    successNotification("Student deleted", `Student ${studentId} has been deleted`);
  })
  .catch(error => {
    error.response.json().then(res => {
      errorNotification("An error occured", res.message)
    })
  })
  .finally(() => {
    callback();
  })
}

const columns = fetchStudents => [
  {
    title: 'Id',
    dataIndex: 'studentId',
    key: 'id'
  },
  {
  title: 'First Name',
  dataIndex: 'firstName',
  key: 'firstname'
},
{
  title: 'Last Name',
  dataIndex: 'lastName',
  key: 'lastname'
},
{
  title: 'Email',
  dataIndex: 'email',
  key: 'email'
},
{
  title: 'Gender',
  dataIndex: 'gender',
  key: 'gender'
},
{
  title: 'Actions',
  dataIndex: 'actions',
  render: (text, student) =>
    <Radio.Group>
      <Popconfirm
        placement='topRight'
        title={`Are you sure to delete ${student.firstName} ${student.lastName}`}
        onConfirm={() => removeStudent(student.studentId, fetchStudents)}
        okText='Yes'
        cancelText='No'>
          <Radio.Button value="small">Delete</Radio.Button>
      </Popconfirm>
    </Radio.Group>
}
];

const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />

function App(){

  const [students, setStudents] = useState([]);
  const [fetching, setFetching] = useState(false);
  const [collapsed, setCollapsed] = useState(false);
  const [showDrawer, setShowDrawer] = useState(false);
  

  const renderStudents = () => {
    if (fetching) {
      return <Spin indicator={antIcon} />
    }
    if(students.length <=0){
      return <>
      <Button
      onClick={() => setShowDrawer(!showDrawer)} 
      type="primary" shape="round" icon={<PlusOutlined />} size="small">
      Add New Student
      </Button>
      <StudentDrawerForm
      showDrawer={showDrawer}
      setShowDrawer={setShowDrawer}
      fetchStudents={fetchStudents}
      /> 
      <Empty />
      </>;
    }
    return <>
    <StudentDrawerForm
      showDrawer={showDrawer}
      setShowDrawer={setShowDrawer}
      fetchStudents={fetchStudents}
      />
    <Table 
    dataSource={students} 
    columns={columns(fetchStudents)}
    title={() => 
      <>
      <Tag>Number of students</Tag>
      <Badge count={students.length} className="site-badge-count-4"/>
      <br/><br/> 
      <Button
      onClick={() => setShowDrawer(!showDrawer)} 
      type="primary" shape="round" icon={<PlusOutlined />} size="small">
      Add New Student
      </Button>
      </>
      }
    pagination={{ pageSize: 50 }}
    scroll={{ y: 260 }} 
    rowKey={(student) => student.id}
    />
    </>
  }

  const fetchStudents = () => {
    setFetching(true)
    getAllStudents()
    .then(res => res.json()
    .then(students => {
      setStudents(students);
      setFetching(false);
    }))
    .catch(error => {
      error.response.json().then(res =>{
        errorNotification("An error occured", res.message);
      })
    })
    .finally(() => setFetching(false))
  }

  useEffect(() => {
    fetchStudents()
  }, []);


  return <Layout style={{ minHeight: '100vh' }}>
  <Sider collapsible collapsed={collapsed}
         onCollapse={setCollapsed}>
      <div className="logo" />
      <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline">
          <Menu.Item key="1" icon={<PieChartOutlined />}>
              Option 1
          </Menu.Item>
          <Menu.Item key="2" icon={<DesktopOutlined />}>
              Option 2
          </Menu.Item>
          <SubMenu key="sub1" icon={<UserOutlined />} title="User">
              <Menu.Item key="3">Tom</Menu.Item>
              <Menu.Item key="4">Bill</Menu.Item>
              <Menu.Item key="5">Alex</Menu.Item>
          </SubMenu>
          <SubMenu key="sub2" icon={<TeamOutlined />} title="Team">
              <Menu.Item key="6">Team 1</Menu.Item>
              <Menu.Item key="8">Team 2</Menu.Item>
          </SubMenu>
          <Menu.Item key="9" icon={<FileOutlined />}>
              Files
          </Menu.Item>
      </Menu>
  </Sider>
  <Layout className="site-layout">
      <Header className="site-layout-background" style={{ padding: 0 }} />
      <Content style={{ margin: '0 16px' }}>
          <Breadcrumb style={{ margin: '16px 0' }}>
              <Breadcrumb.Item>User</Breadcrumb.Item>
              <Breadcrumb.Item>Bill</Breadcrumb.Item>
          </Breadcrumb>
          <div className="site-layout-background" style={{ padding: 24, minHeight: 360 }}>
              {renderStudents()}
          </div>
      </Content>
      <Footer style={{ textAlign: 'center' }}>Footer</Footer>
  </Layout>
</Layout>
}

export default App;