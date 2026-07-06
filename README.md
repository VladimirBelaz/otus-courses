# Домашняя работа №4: Развертывание Selenoid Moon с помощью Ansible

## Описание
Данный проект автоматизирует развертывание Selenoid Moon (платформы для запуска браузеров в Kubernetes) на локальной машине с помощью Ansible.

## Структура ролей
- **common** — установка базовых пакетов (curl, gpg, apt-transport-https)
- **docker** — установка Docker
- **helm** — установка Helm
- **minikube** — установка и запуск Minikube
- **moon** — развертывание Selenoid Moon (browser-ops)

## Требования
- Ubuntu 24.04+
- Ansible 2.16+
- Python 3.12+
- Kubernetes Python библиотека (kubernetes)

## Запуск
```bash
cd ~/roles/moon_roles
ansible-playbook -i ./hosts helm_playbook.yaml -K