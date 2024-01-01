# developeriq-cc
This project was done as a partial completion of the course **CMM707 - Cloud Computing**.
The following Tech-stack has been used in this project.
- Spring/Java
- AWS Web Services
- GitHub
- Jenkins
- Ansible
- Docker
- Kubernetes

## DevOps Flow with Instructions
The steps to configure this CI/CD pipeline are given below.

### Create EKSCTL_ROLE with the following permissions.
- IAMFullAccess
- AmazonEC2FullAccess
- AWSCloudFormationFullAccess

### Create an RDS database for DEV, UAT, and PROD environments and create databases.
- Create `developeriqdev` as the initial database.
- Execute following commands to create `developeriquat` and `developeriqprod`.
```bash
create database developeriquat;
create database developeriqprod; 
```

### Create EC2 Instances
- CMM707_Jenkins_Server
- CMM707_Ansible_Server
- CMM707_Kubernetes_Server

### Install Jenkins on CMM707_Jenkins_Server
```bash
sudo su -
cd /root
sudo yum update
sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io-2023.key
sudo yum upgrade
sudo dnf install java-17-amazon-corretto -y
sudo yum install jenkins -y
```

- Check Jenkins Status
```bash
service jenkins start
service jenkins status
```

### Install Maven and set JDK and Maven configurations in Jenkins
- Dowload Maven.
```bash
cd /opt
wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
tar -xvzf apache-maven-3.9.6-bin.tar.gz
mv apache-maven-3.9.6 maven
```

- Set Java and Maven configurations in `~/.bash_profile`
```bash
# .bash_profile
# Get the aliases and functions
if [ -f ~/.bashrc ]; then
. ~/.bashrc
fi

M2_HOME=/opt/maven
M2=/opt/maven/bin
JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto.x86_64
# User specific environment and startup programs

PATH=$PATH:$HOME/bin:$JAVA_HOME:$M2_HOME:$M2
export PATH
```

- Set Jenkins Maven and JDK configurations by going to `Manage Jenkins` →  `Settings`

### Install Git and Configure it on Jenkins
- Install Git.
```bash
yum install git
```
- Set Jenkins Git configurations by going to `Manage Jenkins` →  `Settings`

### Install Ansible on CMM707_Ansible_Server
```bash
sudo su -
useradd ansadmin
passwd ansadmin
sudo amazon-linux-extras install ansible2
python --version
ansible --version
```

- Update `visudo` file.
```bash
...
ansadmin ALL=(ALL)  NOPASSWD: ALL
...
```

- Update `/etc/ssh/sshd_config` file.
```bash
...
PasswordAuthentication yes
# PasswordAuthentication no
...
```

- Reload `sshd` service.
```bash
reload sshd.service
```

### Install Docker on CMM707_Ansible_Server
- Install Docker.
```bash
sudo su - ansadmin
sudo yum install docker -y
sudo usermod -aG docker ansadmin
sudo service docker start
```

### Create `/opt/docker` directory and add Dockerfile.
- Create `/opt/docker` directory.
```bash
sudo mkdir /opt/docker
cd /opt
chown -R ansadmin:ansadmin docker
```

### Login to DockerHub
- Login to DockerHub
```bash
docker login
```

### Install `kubectl` and `eksctl` on CMM707_Kubernetes_Server
- Install `awscli`.
```bash
sudo su -
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscli2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- Install `kubectl`.
```bash
curl -O https://s3.us-west-2.amazonaws.com/amazon-eks/1.28.3/2023-11-14/bin/linux/amd64/kubectl
chmod +x kubectl
mkdir -p $HOME/bin && cp ./kubectl $HOME/bin/kubectl && export PATH=$HOME/bin:$PATH
```

- Install `eksctl`.
```
ARCH=amd64
PLATFORM=$(uname -s)_$ARCH
curl -sLO "https://github.com/eksctl-io/eksctl/releases/latest/download/eksctl_$PLATFORM.tar.gz"
curl -sL "https://github.com/eksctl-io/eksctl/releases/latest/download/eksctl_checksums.txt" | grep $PLATFORM | sha256sum --check
tar -xzf eksctl_$PLATFORM.tar.gz -C /tmp && rm eksctl_$PLATFORM.tar.gz
sudo mv /tmp/eksctl /usr/local/bin
```

### Enable communication between CMM707_Ansible_Server and CMM707_Kubernetes_Server
- Create a new user named `ansadmin` in CMM707_Kubernetes_Server by following how you created and configured `ansadmin` user in CMM707_Ansible_Server.

- Create `/opt/docker/hosts` file in CMM707_Ansible_Server.
```bash
localhost 

[kubernetes]
178.1.11.2

[ansadmin]
176.1.23.2
```

- Generate SSH Key and share it with CMM707_Kubernetes_Server
```bash
sudo su - ansadmin
ssh-keygen
ssh-copy-id 178.1.11.2
```

### Create Ansible playbooks in CMM707_Ansible_Server

- `create_image_developeriq.yml`
```bash:create_image_developeriq.yml
---
- hosts: ansible
  tasks:
  - name: create docker image
    command: docker build -t developeriq:latest .
    args:
      chdir: /opt/docker
  - name: create tag to push image onto dockerhub
    command: docker tag developeriq:latest nipunaupeksha/developeriq:latest
  - name: push docker image
    command: docker push nipunaupeksha/developeriq:latest
```
- `kube_deploy_dev.yml`
```yaml
---
- hosts: kubernetes
  #become: true
  user: root
  tasks:
  - name: deploy developeriq app deployment on kubernetes dev environment
    command: kubectl apply -f developeriq-dev-deployment.yml
  - name: create a service for developeriq app deployment on kubernetes dev environment
    command: kubectl apply -f developeriq-dev-service.yml
  - name: update deployment with new pods if image updated in dockerhub
    command: kubectl rollout restart deployment.apps/developeriq-dev
```
- `kube_deploy_uat.yml`
```yaml
---
- hosts: kubernetes
  #become: true
  user: root
  tasks:
  - name: deploy developeriq app deployment on kubernetes uat environment
    command: kubectl apply -f developeriq-uat-deployment.yml
  - name: create a service for developeriq app deployment on kubernetes uat environment
    command: kubectl apply -f developeriq-uat-service.yml
  - name: update deployment with new pods if image updated in dockerhub
    command: kubectl rollout restart deployment.apps/developeriq-uat
```
- `kube_deploy_prod.yml`
```yaml
---
- hosts: kubernetes
  #become: true
  user: root
  tasks:
  - name: deploy developeriq app deployment on kubernetes prod environment
    command: kubectl apply -f developeriq-prod-deployment.yml
  - name: create a service for developeriq app deployment on kubernetes prod environment
    command: kubectl apply -f developeriq-prod-service.yml
  - name: update deployment with new pods if image updated in dockerhub
    command: kubectl rollout restart deployment.apps/developeriq-prod
```

### Create Deployment and Service `.yml` files in CMM707_Kubernetes_Server
- `developeriq-dev-deployment.yml`
```yaml
apiVersion: apps/v1 
kind: Deployment
metadata:
  name: developeriq-dev
  labels: 
     app: developeriq-dev

spec:
  replicas: 1 
  selector:
    matchLabels:
      app: developeriq-dev

  template:
    metadata:
      labels:
        app: developeriq-dev
    spec:
      containers:
      - name: developeriq-dev
        image: nipunaupeksha/developeriq
        env:
        - name: SQL_URL
          value: jdbc:mysql://developeriq-1.xx.us-east-1.rds.amazonaws.com:3306/developeriqdev
        - name: SQL_USER
          value: admin
        - name: SQL_PASSWORD
          value: cmm707db
        - name: PAT_TOKEN
          value: github_pat_xx
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 1
```
- `developeriq-dev-service.yml`
```yaml
apiVersion: v1
kind: Service
metadata:
  name: developeriq-dev-service
  labels:
    app: developeriq-dev
spec:
  selector:
    app: developeriq-dev

  ports:
    - port: 8080
      targetPort: 8080

  type: LoadBalancer
```
- `developeriq-uat-deployment.yml`
```yaml
apiVersion: apps/v1 
kind: Deployment
metadata:
  name: developeriq-uat
  labels: 
     app: developeriq-uat

spec:
  replicas: 1 
  selector:
    matchLabels:
      app: developeriq-uat

  template:
    metadata:
      labels:
        app: developeriq-uat
    spec:
      containers:
      - name: developeriq-uat
        image: nipunaupeksha/developeriq
        env:
        - name: SQL_URL
          value: jdbc:mysql://developeriq-1.xx.us-east-1.rds.amazonaws.com:3306/developeriquat
        - name: SQL_USER
          value: admin
        - name: SQL_PASSWORD
          value: cmm707db
        - name: PAT_TOKEN
          value: github_pat_xx
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 1
```
- `developeriq-uat-service.yml`
```yaml
apiVersion: v1
kind: Service
metadata:
  name: developeriq-uat-service
  labels:
    app: developeriq-uat 
spec:
  selector:
    app: developeriq-uat 

  ports:
    - port: 8080
      targetPort: 8080

  type: LoadBalancer
```
- `developeriq-prod-deployment.yml`
```yaml
apiVersion: apps/v1 
kind: Deployment
metadata:
  name: developeriq-prod
  labels: 
     app: developeriq-prod

spec:
  replicas: 2 
  selector:
    matchLabels:
      app: developeriq-prod

  template:
    metadata:
      labels:
        app: developeriq-prod
    spec:
      containers:
      - name: developeriq-prod
        image: nipunaupeksha/developeriq
        env:
        - name: SQL_URL
          value: jdbc:mysql://developeriq-1.xx.us-east-1.rds.amazonaws.com:3306/developeriqprod
        - name: SQL_USER
          value: admin
        - name: SQL_PASSWORD
          value: cmm707db
        - name: PAT_TOKEN
          value: github_pat_xx
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 1
```
- `developeriq-prod-service.yml`
```yaml
apiVersion: v1
kind: Service
metadata:
  name: developeriq-prod-service
  labels:
    app: developeriq-prod 
spec:
  selector:
    app: developeriq-prod 

  ports:
    - port: 8080
      targetPort: 8080

  type: LoadBalancer
```

### Create Jenkins Pipelines and Deploy the deployments
- Use Jenkins `post-build options` to integrate builds properly and create the CI/CD pipeline.
